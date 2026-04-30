package com.restaurant.rating.repository;

import com.restaurant.rating.entity.Review;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ReviewRepository {
    private final List<Review> reviews = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Review save(Review review) {
        // Можно добавить внутреннее id если нужно
        reviews.add(review);
        return review;
    }

    public boolean remove(Long visitorId, Long restaurantId) {
        return reviews.removeIf(r ->
                r.getVisitorId().equals(visitorId) &&
                        r.getRestaurantId().equals(restaurantId));
    }

    public List<Review> findAll() {
        return new ArrayList<>(reviews);
    }

    public List<Review> findByRestaurantId(Long restaurantId) {
        return reviews.stream()
                .filter(r -> r.getRestaurantId().equals(restaurantId))
                .toList();
    }
}