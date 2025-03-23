package org.service;

import org.domain.model.Reservation;
import org.domain.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    public List<Reservation> getAllReservation();

    public void addReservation(Reservation reservation);

    public void removeReservationById(long id);

    public List<Reservation> getPersonalReservation(User user);

    public boolean userAddReservation(
            long id, String reservationName, LocalDate bookingDate,
            LocalDateTime startDateTime, LocalDateTime endDateTime,
            User user, CoworkingSpaceServiceImpl coworkingSpaceService,
            ReservationServiceImpl reservationService);
}
