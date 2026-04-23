package com.restaurant.rating.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Visitor {
    private Long id;
    private String name; // nullable - анонимные отзывы
    private Integer age;
    private String gender;
}