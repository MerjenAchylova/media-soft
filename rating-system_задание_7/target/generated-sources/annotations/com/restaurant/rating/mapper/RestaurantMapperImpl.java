package com.restaurant.rating.mapper;

import com.restaurant.rating.dto.RestaurantRequestDTO;
import com.restaurant.rating.dto.RestaurantResponseDTO;
import com.restaurant.rating.entity.Restaurant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-08T00:13:35+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 15.0.2 (Oracle Corporation)"
)
@Component
public class RestaurantMapperImpl implements RestaurantMapper {

    @Override
    public Restaurant toEntity(RestaurantRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Restaurant.RestaurantBuilder restaurant = Restaurant.builder();

        restaurant.name( dto.getName() );
        restaurant.description( dto.getDescription() );
        restaurant.cuisineType( dto.getCuisineType() );
        restaurant.averageCheck( dto.getAverageCheck() );

        return restaurant.build();
    }

    @Override
    public RestaurantResponseDTO toResponse(Restaurant entity) {
        if ( entity == null ) {
            return null;
        }

        RestaurantResponseDTO.RestaurantResponseDTOBuilder restaurantResponseDTO = RestaurantResponseDTO.builder();

        restaurantResponseDTO.id( entity.getId() );
        restaurantResponseDTO.name( entity.getName() );
        restaurantResponseDTO.description( entity.getDescription() );
        restaurantResponseDTO.cuisineType( entity.getCuisineType() );
        restaurantResponseDTO.averageCheck( entity.getAverageCheck() );
        restaurantResponseDTO.userRating( entity.getUserRating() );

        return restaurantResponseDTO.build();
    }
}
