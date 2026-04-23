package com.restaurant.rating.repository;

import com.restaurant.rating.entity.Visitor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class VisitorRepository {
    private final List<Visitor> visitors = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Visitor save(Visitor visitor) {
        if (visitor.getId() == null) {
            visitor.setId(idGenerator.getAndIncrement());
        }
        visitors.add(visitor);
        return visitor;
    }

    public boolean remove(Long id) {
        return visitors.removeIf(v -> v.getId().equals(id));
    }

    public List<Visitor> findAll() {
        return new ArrayList<>(visitors);
    }
}