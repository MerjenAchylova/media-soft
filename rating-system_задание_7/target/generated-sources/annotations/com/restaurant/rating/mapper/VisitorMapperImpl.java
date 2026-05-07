package com.restaurant.rating.mapper;

import com.restaurant.rating.dto.VisitorRequestDTO;
import com.restaurant.rating.dto.VisitorResponseDTO;
import com.restaurant.rating.entity.Visitor;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-08T00:13:35+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 15.0.2 (Oracle Corporation)"
)
@Component
public class VisitorMapperImpl implements VisitorMapper {

    @Override
    public Visitor toEntity(VisitorRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Visitor.VisitorBuilder visitor = Visitor.builder();

        visitor.name( dto.getName() );
        visitor.age( dto.getAge() );
        visitor.gender( dto.getGender() );

        return visitor.build();
    }

    @Override
    public VisitorResponseDTO toResponse(Visitor entity) {
        if ( entity == null ) {
            return null;
        }

        VisitorResponseDTO.VisitorResponseDTOBuilder visitorResponseDTO = VisitorResponseDTO.builder();

        visitorResponseDTO.id( entity.getId() );
        visitorResponseDTO.name( entity.getName() );
        visitorResponseDTO.age( entity.getAge() );
        visitorResponseDTO.gender( entity.getGender() );

        return visitorResponseDTO.build();
    }
}
