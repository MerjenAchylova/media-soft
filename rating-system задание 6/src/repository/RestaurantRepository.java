package com.restaurant.rating.repository;

import com.restaurant.rating.entity.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RestaurantRepository {
    private final List<Restaurant> restaurants = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Restaurant save(Restaurant restaurant) {
        if (restaurant.getId() == null) {
            restaurant.setId(idGenerator.getAndIncrement());
        }
        // Если ресторан уже есть — обновляем
        Optional<Restaurant> existing = findById(restaurant.getId());
        if (existing.isPresent()) {
            remove(restaurant.getId());
        }
        restaurants.add(restaurant);
        return restaurant;
    }

    public boolean remove(Long id) {
        return restaurants.removeIf(r -> r.getId().equals(id));
    }

    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants);
    }

    public Optional<Restaurant> findById(Long id) {
        return restaurants.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }
}