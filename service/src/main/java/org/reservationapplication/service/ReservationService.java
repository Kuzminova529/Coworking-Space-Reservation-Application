package org.reservationapplication.service;

import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    List<Reservation> getAllReservation();

    void addReservation(Reservation reservation);

    void removeReservationById(long id);

    List<Reservation> getPersonalReservation(User user);

    boolean userAddReservation(
            long id, String reservationName, LocalDate bookingDate,
            LocalDateTime startDateTime, LocalDateTime endDateTime,
            User user, CoworkingSpaceService coworkingSpaceService);
}
