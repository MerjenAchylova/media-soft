package com.restaurant.rating.dto;

import lombok.*;
import jakarta.validation.constraints.*; // или javax.validation.constraints.* для Boot 2.7

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class VisitorRequestDTO {
    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotNull(message = "Возраст обязателен")
    @Min(value = 0, message = "Возраст не может быть отрицательным")
    private Integer age;

    @NotBlank(message = "Пол обязателен")
    private String gender;
}