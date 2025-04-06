package org.reservationapplication.service;

import org.reservationapplication.domain.dto.ReservationDto;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationService {
    ReservationDto findReservationById(Long id);

    List<ReservationDto> getAllReservation();

    List<ReservationDto> getPersonalReservation(Long id);

    ReservationDto addReservation(ReservationDto reservation);

    ReservationDto userAddReservation(
            long id, String reservationName, LocalDate bookingDate,
            LocalDateTime startDateTime, LocalDateTime endDateTime,
            User user, CoworkingSpaceService coworkingSpaceService);

    boolean removeReservationById(long id);
}
