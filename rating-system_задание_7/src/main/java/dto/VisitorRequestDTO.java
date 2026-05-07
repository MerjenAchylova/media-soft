package com.restaurant.rating.dto;

import lombok.*;
import javax.validation.constraints.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class VisitorRequestDTO {
    @NotBlank(message = "Имя обязательно")
    private String name;

    @NotNull(message = "Возраст обязателен")
    @Min(value = 0, message = "Возраст не может быть отрицательным")
    private Integer age;

    @NotBlank(message = "Пол обязателен")
    private String gender;
}