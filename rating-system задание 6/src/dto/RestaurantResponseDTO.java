package com.restaurant.rating.dto;

import com.restaurant.rating.entity.CuisineType;
import lombok.*;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RestaurantResponseDTO {
    private Long id;
    private String name;
    private String description;
    private CuisineType cuisineType;
    private BigDecimal averageCheck;
    private BigDecimal userRating;
}