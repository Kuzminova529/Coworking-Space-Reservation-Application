package org.reservationapplication.service;

import org.reservationapplication.model.Customer;
import org.reservationapplication.model.Reservation;
import org.reservationapplication.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TreeSet;

public interface ReservationService {
    public TreeSet<Reservation> getAllReservation();

    public void addReservation(Reservation reservation);

    public void removeReservationById(long id);

    public TreeSet<Reservation> getPersonalReservation(User user);

    public boolean userAddReservation(
            long id, String reservationName, LocalDate bookingDate,
            LocalDateTime startDateTime, LocalDateTime endDateTime,
            User user, CoworkingSpaceServiceImpl coworkingSpaceService,
            ReservationServiceImpl reservationService);
}
