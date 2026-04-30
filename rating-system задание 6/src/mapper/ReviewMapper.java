package com.restaurant.rating.mapper;

import com.restaurant.rating.dto.ReviewRequestDTO;
import com.restaurant.rating.dto.ReviewResponseDTO;
import com.restaurant.rating.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toEntity(ReviewRequestDTO dto);
    ReviewResponseDTO toResponse(Review entity);
}