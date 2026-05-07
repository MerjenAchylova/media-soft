package com.restaurant.rating.repository;

import com.restaurant.rating.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // Требование 3: Через конвенцию имен
    List<Restaurant> findByUserRatingGreaterThanEqual(BigDecimal minRating);

    // Требование 3: Через @Query и JPQL
    @Query("SELECT r FROM Restaurant r WHERE r.userRating >= :minRating ORDER BY r.userRating DESC")
    List<Restaurant> findRestaurantsWithMinRatingJPQL(@Param("minRating") BigDecimal minRating);
}