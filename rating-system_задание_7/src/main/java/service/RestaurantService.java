package com.restaurant.rating.service;

import com.restaurant.rating.dto.RestaurantRequestDTO;
import com.restaurant.rating.dto.RestaurantResponseDTO;
import com.restaurant.rating.mapper.RestaurantMapper;
import com.restaurant.rating.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository repo;
    private final RestaurantMapper mapper;

    public RestaurantResponseDTO save(RestaurantRequestDTO dto) {
        var entity = mapper.toEntity(dto);
        entity.setUserRating(BigDecimal.ZERO);
        return mapper.toResponse(repo.save(entity));
    }

    public boolean remove(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public List<RestaurantResponseDTO> findAll() {
        return repo.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public void updateRating(Long id, BigDecimal rating) {
        repo.findById(id).ifPresent(r -> {
            r.setUserRating(rating);
            repo.save(r);
        });
    }

    // Требование 3: Поиск по рейтингу
    public List<RestaurantResponseDTO> findByMinRating(BigDecimal minRating, boolean useJpql) {
        var list = useJpql ? repo.findRestaurantsWithMinRatingJPQL(minRating)
                : repo.findByUserRatingGreaterThanEqual(minRating);
        return list.stream().map(mapper::toResponse).collect(Collectors.toList());
    }
}