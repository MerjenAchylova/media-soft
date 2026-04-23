package com.restaurant.rating.service;

import com.restaurant.rating.entity.Restaurant;
import com.restaurant.rating.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public Restaurant save(Restaurant restaurant) {
        validate(restaurant);
        if (restaurant.getUserRating() == null) {
            restaurant.setUserRating(BigDecimal.ZERO);
        }
        return restaurantRepository.save(restaurant);
    }

    public boolean remove(Long id) {
        return restaurantRepository.remove(id);
    }

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public void updateRating(Long restaurantId, BigDecimal newRating) {
        restaurantRepository.findById(restaurantId).ifPresent(restaurant -> {
            restaurant.setUserRating(newRating);
            restaurantRepository.save(restaurant);
        });
    }

    private void validate(Restaurant restaurant) {
        if (restaurant.getName() == null || restaurant.getName().isBlank()) {
            throw new IllegalArgumentException("Restaurant name is required");
        }
        if (restaurant.getCuisineType() == null) {
            throw new IllegalArgumentException("Cuisine type is required");
        }
        if (restaurant.getAverageCheck() == null ||
                restaurant.getAverageCheck().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Average check must be positive");
        }
    }
}