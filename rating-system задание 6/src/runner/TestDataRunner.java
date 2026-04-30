package com.restaurant.rating.runner;

import com.restaurant.rating.entity.*;
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

        // Создаём посетителей
        Visitor v1 = visitorService.save(Visitor.builder()
                .name("Ilyesa")
                .age(25)
                .gender("M")
                .build());

        Visitor v2 = visitorService.save(Visitor.builder()
                .name("Anna")
                .age(30)
                .gender("F")
                .build());

        // Анонимный посетитель
        Visitor v3 = visitorService.save(Visitor.builder()
                .name(null) // аноним
                .age(22)
                .gender("F")
                .build());

        // Создаём рестораны
        Restaurant r1 = restaurantService.save(Restaurant.builder()
                .name("La Pasta")
                .description("Лучшая итальянская кухня")
                .cuisineType(CuisineType.ITALIAN)
                .averageCheck(new BigDecimal("1500.00"))
                .build());

        Restaurant r2 = restaurantService.save(Restaurant.builder()
                .name("Wok&Roll")
                .description(null) // пустое описание
                .cuisineType(CuisineType.CHINESE)
                .averageCheck(new BigDecimal("900.00"))
                .build());

        // Добавляем оценки
        reviewService.save(Review.builder()
                .visitorId(v1.getId())
                .restaurantId(r1.getId())
                .rating(9)
                .comment("Ochen' vkusno!")
                .build());

        reviewService.save(Review.builder()
                .visitorId(v2.getId())
                .restaurantId(r1.getId())
                .rating(10)
                .comment("Ideal'no!")
                .build());

        reviewService.save(Review.builder()
                .visitorId(v3.getId()) // аноним
                .restaurantId(r2.getId())
                .rating(7)
                .comment(null)
                .build());

        reviewService.save(Review.builder()
                .visitorId(v1.getId())
                .restaurantId(r2.getId())
                .rating(8)
                .comment("Horosho, no mojno luchshe")
                .build());

        // Вывод результатов
        System.out.println("\n📊 Рестораны и их рейтинги:");
        restaurantService.findAll().forEach(r ->
                System.out.printf("  %s (%s): %.2f ⭐\n",
                        r.getName(),
                        r.getCuisineType(),
                        r.getUserRating())
        );

        System.out.println("\n👥 Посетители:");
        visitorService.findAll().forEach(v ->
                System.out.printf("  ID:%d | %s | %d let | %s\n",
                        v.getId(),
                        v.getName() != null ? v.getName() : "[ANONIM]",
                        v.getAge(),
                        v.getGender())
        );

        System.out.println("\n✅ Testy zaversheny!");
    }
}