package com.restaurant.rating.dto;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class VisitorResponseDTO {
    private Long id;
    private String name;
    private Integer age;
    private String gender;
}