package org.reservationapplication.web.mapper;

import org.reservationapplication.domain.builder.ReservationBuilder;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.service.CoworkingSpaceService;
import org.reservationapplication.web.dto.ReservationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DTOReservationMapper {

    private CoworkingSpaceService coworkingSpaceService;

    @Autowired
    public DTOReservationMapper(@Qualifier("coworkingSpaceServiceImpl") CoworkingSpaceService coworkingSpaceService) {
        this.coworkingSpaceService = coworkingSpaceService;
    }


    public ReservationDto toDto(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                reservation.getCoworkingSpace().getId(),
                reservation.getUserID(),
                reservation.getReservationName(),
                reservation.getStartDateTime(),
                reservation.getEndDateTime(),
                reservation.getActive()
        );
    }

    public Reservation toEntity(ReservationDto dto) {
        CoworkingSpace coworkingSpace = coworkingSpaceService.getCoworkingSpaceByIDForReservation(dto.getCoworkingSpaceId());
        Reservation reservation = new ReservationBuilder()
                .setCoworkingSpace(coworkingSpace)
                .setReservationName(dto.getReservationName())
                .setStartDateTime(dto.getStartDateTime())
                .setEndDateTime(dto.getEndDateTime())
                .setUserId(dto.getUserID())
                .setActive(true)
                .build();

        return reservation;
    }
}
