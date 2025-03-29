package org.reservationapplication.service;

import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    List<Reservation> getAllReservation();

    List<Reservation> getPersonalReservation(Long id);

    Reservation addReservation(Reservation reservation);

    Reservation userAddReservation(
            long id, String reservationName, LocalDate bookingDate,
            LocalDateTime startDateTime, LocalDateTime endDateTime,
            User user, CoworkingSpaceService coworkingSpaceService);

    boolean removeReservationById(long id);

}
