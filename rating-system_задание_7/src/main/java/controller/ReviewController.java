package com.restaurant.rating.controller;

import com.restaurant.rating.dto.ReviewRequestDTO;
import com.restaurant.rating.dto.ReviewResponseDTO;
import com.restaurant.rating.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

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

    // Требование 2: Пагинация и сортировка
    @GetMapping
    @Operation(summary = "Получить отзывы с пагинацией")
    public ResponseEntity<Page<ReviewResponseDTO>> getAll(
            @PageableDefault(size = 10, sort = "rating", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.findAllPaginated(pageable));
    }

    @DeleteMapping
    @Operation(summary = "Удалить отзыв")
    public ResponseEntity<Void> delete(@RequestParam Long visitorId, @RequestParam Long restaurantId) {
        service.remove(visitorId, restaurantId);
        return ResponseEntity.noContent().build();
    }
}