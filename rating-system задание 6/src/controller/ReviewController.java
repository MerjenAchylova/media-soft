package com.restaurant.rating.controller;

import com.restaurant.rating.dto.ReviewRequestDTO;
import com.restaurant.rating.dto.ReviewResponseDTO;
import com.restaurant.rating.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Управление отзывами")
public class ReviewController {
    private final ReviewService service;

    @PostMapping
    @Operation(summary = "Добавить отзыв")
    public ResponseEntity<ReviewResponseDTO> create(@Valid @RequestBody ReviewRequestDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping
    @Operation(summary = "Получить все отзывы")
    public ResponseEntity<List<ReviewResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping
    @Operation(summary = "Удалить отзыв")
    public ResponseEntity<Void> delete(@RequestParam Long visitorId, @RequestParam Long restaurantId) {
        service.remove(visitorId, restaurantId);
        return ResponseEntity.noContent().build();
    }
}