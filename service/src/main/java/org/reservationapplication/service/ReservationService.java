package org.reservationapplication.service;

import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    Reservation findReservationById(Long id);

    List<Reservation> getAllReservation();

    List<Reservation> getPersonalReservation(Long id);

    Reservation addReservation(Reservation reservation);

    boolean removeReservation(long id);

    boolean isTimeSlotAvailable(Long coworkingSpaceId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
