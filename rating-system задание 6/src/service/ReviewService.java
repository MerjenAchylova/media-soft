package com.restaurant.rating.service;

import com.restaurant.rating.dto.ReviewRequestDTO;
import com.restaurant.rating.dto.ReviewResponseDTO;
import com.restaurant.rating.mapper.ReviewMapper;
import com.restaurant.rating.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepo;
    private final RestaurantService restService;
    private final ReviewMapper mapper;

    public ReviewResponseDTO save(ReviewRequestDTO dto) {
        var entity = mapper.toEntity(dto);
        reviewRepo.save(entity);
        recalc(entity.getRestaurantId());
        return mapper.toResponse(entity);
    }

    public boolean remove(Long vId, Long rId) {
        boolean ok = reviewRepo.remove(vId, rId);
        if (ok) recalc(rId);
        return ok;
    }

    public List<ReviewResponseDTO> findAll() {
        return reviewRepo.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    private void recalc(Long rId) {
        var list = reviewRepo.findByRestaurantId(rId);
        if (list.isEmpty()) { restService.updateRating(rId, BigDecimal.ZERO); return; }
        var sum = list.stream().map(r -> BigDecimal.valueOf(r.getRating())).reduce(BigDecimal.ZERO, BigDecimal::add);
        restService.updateRating(rId, sum.divide(BigDecimal.valueOf(list.size()), 2, RoundingMode.HALF_UP));
    }
}