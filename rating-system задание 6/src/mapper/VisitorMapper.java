package com.restaurant.rating.mapper;

import com.restaurant.rating.dto.VisitorRequestDTO;
import com.restaurant.rating.dto.VisitorResponseDTO;
import com.restaurant.rating.entity.Visitor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VisitorMapper {
    Visitor toEntity(VisitorRequestDTO dto);
    VisitorResponseDTO toResponse(Visitor entity);
}