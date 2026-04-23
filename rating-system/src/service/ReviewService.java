package com.restaurant.rating.service;

import com.restaurant.rating.entity.Review;
import com.restaurant.rating.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestaurantService restaurantService;

    public Review save(Review review) {
        validate(review);
        reviewRepository.save(review);
        recalculateRestaurantRating(review.getRestaurantId());
        return review;
    }

    public boolean remove(Long visitorId, Long restaurantId) {
        boolean removed = reviewRepository.remove(visitorId, restaurantId);
        if (removed) {
            recalculateRestaurantRating(restaurantId);
        }
        return removed;
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public List<Review> findByRestaurantId(Long restaurantId) {
        return reviewRepository.findByRestaurantId(restaurantId);
    }

    /** Пересчитывает среднюю оценку ресторана */
    private void recalculateRestaurantRating(Long restaurantId) {
        List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);

        if (reviews.isEmpty()) {
            restaurantService.updateRating(restaurantId, BigDecimal.ZERO);
            return;
        }

        BigDecimal sum = reviews.stream()
                .map(Review::getRating)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal average = sum.divide(
                BigDecimal.valueOf(reviews.size()),
                2,
                RoundingMode.HALF_UP
        );

        restaurantService.updateRating(restaurantId, average);
    }

    private void validate(Review review) {
        if (review.getVisitorId() == null) {
            throw new IllegalArgumentException("Visitor ID is required");
        }
        if (review.getRestaurantId() == null) {
            throw new IllegalArgumentException("Restaurant ID is required");
        }
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 10) {
            throw new IllegalArgumentException("Rating must be between 1 and 10");
        }
    }
}