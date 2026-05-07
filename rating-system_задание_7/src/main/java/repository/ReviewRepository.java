package com.restaurant.rating.repository;

import com.restaurant.rating.entity.Review;
import com.restaurant.rating.entity.ReviewId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, ReviewId> {
    List<Review> findByIdRestaurantId(Long restaurantId);

    Page<Review> findAllByOrderByIdRestaurantIdAsc(Pageable pageable);
}