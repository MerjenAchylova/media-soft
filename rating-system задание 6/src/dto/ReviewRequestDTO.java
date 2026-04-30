package com.restaurant.rating.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReviewRequestDTO {
    @NotNull(message = "ID посетителя обязателен")
    private Long visitorId;

    @NotNull(message = "ID ресторана обязателен")
    private Long restaurantId;

    @NotNull(message = "Оценка обязательна")
    @Min(value = 1, message = "Оценка от 1 до 10")
    @Max(value = 10, message = "Оценка от 1 до 10")
    private Integer rating;

    private String comment;
}