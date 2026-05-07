package com.restaurant.rating.mapper;

import com.restaurant.rating.dto.ReviewRequestDTO;
import com.restaurant.rating.dto.ReviewResponseDTO;
import com.restaurant.rating.entity.Review;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-08T00:13:35+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 15.0.2 (Oracle Corporation)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public Review toEntity(ReviewRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Review.ReviewBuilder review = Review.builder();

        review.rating( dto.getRating() );
        review.comment( dto.getComment() );

        return review.build();
    }

    @Override
    public ReviewResponseDTO toResponse(Review entity) {
        if ( entity == null ) {
            return null;
        }

        ReviewResponseDTO.ReviewResponseDTOBuilder reviewResponseDTO = ReviewResponseDTO.builder();

        reviewResponseDTO.rating( entity.getRating() );
        reviewResponseDTO.comment( entity.getComment() );

        return reviewResponseDTO.build();
    }
}
