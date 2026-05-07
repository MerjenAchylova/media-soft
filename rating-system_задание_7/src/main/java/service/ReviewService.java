package com.restaurant.rating.service;

import com.restaurant.rating.dto.ReviewRequestDTO;
import com.restaurant.rating.dto.ReviewResponseDTO;
import com.restaurant.rating.entity.Review;
import com.restaurant.rating.entity.ReviewId;
import com.restaurant.rating.mapper.ReviewMapper;
import com.restaurant.rating.repository.ReviewRepository;
import com.restaurant.rating.repository.RestaurantRepository;
import com.restaurant.rating.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service @RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepo;
    private final RestaurantRepository restRepo;
    private final VisitorRepository visitorRepo;
    private final ReviewMapper mapper;

    public ReviewResponseDTO save(ReviewRequestDTO dto) {
        var visitor = visitorRepo.findById(dto.getVisitorId())
                .orElseThrow(() -> new EntityNotFoundException("Visitor not found"));
        var restaurant = restRepo.findById(dto.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        var review = mapper.toEntity(dto);
        review.setVisitor(visitor);
        review.setRestaurant(restaurant);

        reviewRepo.save(review);
        recalcRating(restaurant.getId());
        return mapper.toResponse(review);
    }

    public boolean remove(Long visitorId, Long restaurantId) {
        var id = new ReviewId(visitorId, restaurantId);
        if (reviewRepo.existsById(id)) {
            reviewRepo.deleteById(id);
            recalcRating(restaurantId);
            return true;
        }
        return false;
    }

    // Требование 2: Пагинация
    public Page<ReviewResponseDTO> findAllPaginated(Pageable pageable) {
        return reviewRepo.findAll(pageable).map(mapper::toResponse);
    }

    private void recalcRating(Long restaurantId) {
        var reviews = reviewRepo.findByIdRestaurantId(restaurantId);
        if (reviews.isEmpty()) {
            restRepo.findById(restaurantId).ifPresent(r -> {
                r.setUserRating(BigDecimal.ZERO);
                restRepo.save(r);
            });
            return;
        }
        var sum = reviews.stream()
                .map(Review::getRating)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var avg = sum.divide(BigDecimal.valueOf(reviews.size()), 2, RoundingMode.HALF_UP);

        restRepo.findById(restaurantId).ifPresent(r -> {
            r.setUserRating(avg);
            restRepo.save(r);
        });
    }
}