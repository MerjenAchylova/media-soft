package com.restaurant.rating.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private Long visitorId;
    private Long restaurantId;
    private Integer rating; // 1-10 или 1-5
    private String comment; // может быть пустым
}