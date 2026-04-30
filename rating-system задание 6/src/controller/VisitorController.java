package com.restaurant.rating.controller;

import com.restaurant.rating.dto.VisitorRequestDTO;
import com.restaurant.rating.dto.VisitorResponseDTO;
import com.restaurant.rating.service.VisitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
@Tag(name = "Visitors", description = "Управление посетителями")
public class VisitorController {
    private final VisitorService service;

    @PostMapping
    @Operation(summary = "Создать посетителя")
    @ApiResponse(responseCode = "200", description = "Успешно создан")
    public ResponseEntity<VisitorResponseDTO> create(@Valid @RequestBody VisitorRequestDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping
    @Operation(summary = "Получить всех посетителей")
    public ResponseEntity<List<VisitorResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить посетителя")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
}