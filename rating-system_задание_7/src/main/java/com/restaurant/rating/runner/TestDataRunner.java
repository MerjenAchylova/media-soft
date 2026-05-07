package com.restaurant.rating.runner;

import com.restaurant.rating.dto.*;
import com.restaurant.rating.entity.CuisineType;
import com.restaurant.rating.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TestDataRunner implements CommandLineRunner {

    private final VisitorService visitorService;
    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    @Override
    public void run(String... args) {
        System.out.println("🚀 Запуск тестовых данных...");

        // Создаем посетителей (теперь сервис возвращает DTO!)
        VisitorResponseDTO v1 = visitorService.save(VisitorRequestDTO.builder()
                .name("Alex")
                .age(25)
                .gender("M")
                .build());

        VisitorResponseDTO v2 = visitorService.save(VisitorRequestDTO.builder()
                .name("Maria")
                .age(30)
                .gender("F")
                .build());

        VisitorResponseDTO v3 = visitorService.save(VisitorRequestDTO.builder()
                .name("Anonymous")
                .age(22)
                .gender("F")
                .build());

        // Создаем рестораны
        RestaurantResponseDTO r1 = restaurantService.save(RestaurantRequestDTO.builder()
                .name("La Pasta")
                .description("Лучшая итальянская кухня")
                .cuisineType(CuisineType.ITALIAN)
                .averageCheck(new BigDecimal("1500.00"))
                .build());

        RestaurantResponseDTO r2 = restaurantService.save(RestaurantRequestDTO.builder()
                .name("Wok&Roll")
                .description(null)
                .cuisineType(CuisineType.CHINESE)
                .averageCheck(new BigDecimal("900.00"))
                .build());

        // Добавляем отзывы (используем ID из DTO!)
        reviewService.save(ReviewRequestDTO.builder()
                .visitorId(v1.getId())
                .restaurantId(r1.getId())
                .rating(9)
                .comment("Очень вкусно!")
                .build());

        reviewService.save(ReviewRequestDTO.builder()
                .visitorId(v2.getId())
                .restaurantId(r1.getId())
                .rating(10)
                .comment("Идеально!")
                .build());

        reviewService.save(ReviewRequestDTO.builder()
                .visitorId(v3.getId())
                .restaurantId(r2.getId())
                .rating(7)
                .comment(null)
                .build());

        reviewService.save(ReviewRequestDTO.builder()
                .visitorId(v1.getId())
                .restaurantId(r2.getId())
                .rating(8)
                .comment("Хорошо, но можно лучше")
                .build());

        // Выводим результаты
        System.out.println("\n📊 Рестораны и их рейтинги:");
        restaurantService.findAll().forEach(r ->
                System.out.printf("  %s (%s): %.2f ⭐\n",
                        r.getName(),
                        r.getCuisineType(),
                        r.getUserRating())
        );

        System.out.println("\n✅ Тестовые данные загружены!");
    }
}