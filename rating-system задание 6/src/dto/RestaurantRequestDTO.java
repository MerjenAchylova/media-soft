package com.restaurant.rating.dto;

import com.restaurant.rating.entity.CuisineType;
import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RestaurantRequestDTO {
    @NotBlank(message = "Название обязательно")
    private String name;
    private String description;

    @NotNull(message = "Тип кухни обязателен")
    private CuisineType cuisineType;

    @NotNull(message = "Средний чек обязателен")
    @Positive(message = "Средний чек должен быть положительным")
    private BigDecimal averageCheck;
}