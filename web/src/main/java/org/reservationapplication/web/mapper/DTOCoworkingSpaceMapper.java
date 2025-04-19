package org.reservationapplication.web.mapper;

import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.service.CoworkingSpaceService;
import org.reservationapplication.web.dto.CoworkingSpaceDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DTOCoworkingSpaceMapper {

    public CoworkingSpaceDto toDto(CoworkingSpace space) {
        return new CoworkingSpaceDto(
                space.getId(),
                space.getType(),
                space.getPrice(),
                space.getActive()
        );
    }

    public CoworkingSpace toEntity(CoworkingSpaceDto dto) {
        CoworkingSpace coworkingSpace = new CoworkingSpace();
        coworkingSpace.setType(dto.getType());
        coworkingSpace.setPrice(dto.getPrice());
        coworkingSpace.setActive(dto.isActive());
        return coworkingSpace;
    }

}
