package com.restaurant.rating.dto;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReviewResponseDTO {
    private Long visitorId;
    private Long restaurantId;
    private Integer rating;
    private String comment;
}