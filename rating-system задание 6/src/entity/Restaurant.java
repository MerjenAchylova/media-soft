package com.restaurant.rating.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    private Long id;
    private String name;
    private String description; // может быть пустым
    private CuisineType cuisineType;
    private BigDecimal averageCheck;
    private BigDecimal userRating; // средняя оценка
}