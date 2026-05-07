package com.restaurant.rating.controller;

import com.restaurant.rating.dto.RestaurantRequestDTO;
import com.restaurant.rating.dto.RestaurantResponseDTO;
import com.restaurant.rating.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurants", description = "Управление ресторанами")
public class RestaurantController {
    private final RestaurantService service;

    @PostMapping
    @Operation(summary = "Создать ресторан")
    public ResponseEntity<RestaurantResponseDTO> create(@Valid @RequestBody RestaurantRequestDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping
    @Operation(summary = "Получить все рестораны")
    public ResponseEntity<List<RestaurantResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить ресторан")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }

    // Требование 3: Фильтр по рейтингу
    @GetMapping("/filter")
    @Operation(summary = "Найти рестораны с рейтингом >= заданного")
    public ResponseEntity<List<RestaurantResponseDTO>> filterByRating(
            @RequestParam BigDecimal minRating,
            @RequestParam(defaultValue = "false") boolean useJpql) {
        return ResponseEntity.ok(service.findByMinRating(minRating, useJpql));
    }
}