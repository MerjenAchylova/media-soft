package com.restaurant.rating.service;

import com.restaurant.rating.dto.VisitorRequestDTO;  // ✅ Добавьте этот импорт!
import com.restaurant.rating.dto.VisitorResponseDTO;
import com.restaurant.rating.entity.Visitor;
import com.restaurant.rating.mapper.VisitorMapper;
import com.restaurant.rating.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository repo;
    private final VisitorMapper mapper;

    // ✅ Измените параметр с Visitor на VisitorRequestDTO
    public VisitorResponseDTO save(VisitorRequestDTO dto) {
        return mapper.toResponse(repo.save(mapper.toEntity(dto)));
    }

    public boolean remove(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public List<VisitorResponseDTO> findAll() {
        return repo.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }
}