package com.restaurant.rating.service;

import com.restaurant.rating.entity.Visitor;
import com.restaurant.rating.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;

    public Visitor save(Visitor visitor) {
        validate(visitor);
        return visitorRepository.save(visitor);
    }

    public boolean remove(Long id) {
        return visitorRepository.remove(id);
    }

    public List<Visitor> findAll() {
        return visitorRepository.findAll();
    }

    private void validate(Visitor visitor) {
        if (visitor.getAge() == null || visitor.getAge() < 0) {
            throw new IllegalArgumentException("Age must be positive");
        }
        if (visitor.getGender() == null || visitor.getGender().isBlank()) {
            throw new IllegalArgumentException("Gender is required");
        }
    }
}