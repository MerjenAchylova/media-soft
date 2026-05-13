package com.restaurant.rating.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.rating.dto.RestaurantRequestDTO;
import com.restaurant.rating.dto.RestaurantResponseDTO;
import com.restaurant.rating.entity.CuisineType;
import com.restaurant.rating.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(com.restaurant.rating.controller.RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RestaurantService restaurantService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAll_shouldReturn200AndList() throws Exception {
        RestaurantResponseDTO r1 = RestaurantResponseDTO.builder()
                .id(1L).name("La Pasta").cuisineType(CuisineType.ITALIAN)
                .averageCheck(new BigDecimal("1500")).userRating(new BigDecimal("9.5"))
                .build();

        when(restaurantService.findAll()).thenReturn(List.of(r1));

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("La Pasta"))
                .andExpect(jsonPath("$[0].cuisineType").value("ITALIAN"));
    }

    @Test
    void create_shouldReturn200AndCreatedRestaurant() throws Exception {
        RestaurantRequestDTO request = RestaurantRequestDTO.builder()
                .name("Test Restaurant")
                .description("Great food")
                .cuisineType(CuisineType.CHINESE)
                .averageCheck(new BigDecimal("1000"))
                .build();

        RestaurantResponseDTO response = RestaurantResponseDTO.builder()
                .id(1L).name("Test Restaurant").cuisineType(CuisineType.CHINESE)
                .averageCheck(new BigDecimal("1000")).userRating(BigDecimal.ZERO)
                .build();

        when(restaurantService.save(any())).thenReturn(response);

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Restaurant"))
                .andExpect(jsonPath("$.userRating").value(0.0));
    }

    @Test
    void create_withInvalidData_shouldReturn400() throws Exception {
        RestaurantRequestDTO request = RestaurantRequestDTO.builder()
                .name("")  // пустое имя
                .cuisineType(CuisineType.ITALIAN)
                .averageCheck(new BigDecimal("1000"))
                .build();

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_withNegativeAverageCheck_shouldReturn400() throws Exception {
        RestaurantRequestDTO request = RestaurantRequestDTO.builder()
                .name("Test")
                .cuisineType(CuisineType.ITALIAN)
                .averageCheck(new BigDecimal("-100"))  // отрицательный чек
                .build();

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/restaurants/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void filterByRating_withJpqlFalse_shouldReturn200() throws Exception {
        RestaurantResponseDTO r1 = RestaurantResponseDTO.builder()
                .id(1L).name("Top").userRating(new BigDecimal("9.0")).build();

        when(restaurantService.findByMinRating(eq(new BigDecimal("5.0")), eq(false)))
                .thenReturn(List.of(r1));

        mockMvc.perform(get("/api/restaurants/filter")
                        .param("minRating", "5.0")
                        .param("useJpql", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void filterByRating_withJpqlTrue_shouldReturn200() throws Exception {
        when(restaurantService.findByMinRating(eq(new BigDecimal("7.0")), eq(true)))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/restaurants/filter")
                        .param("minRating", "7.0")
                        .param("useJpql", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}