package org.reservationapplication.service;

import org.reservationapplication.model.Customer;
import org.reservationapplication.model.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TreeSet;

public interface ReservationService {
    public TreeSet<Reservation> getAllReservation();

    public void addReservation(Reservation reservation);

    public boolean removeReservationById(long id);

    public boolean userAddReservation(
            long id, String reservationName, LocalDate bookingDate,
            LocalDateTime startDateTime, LocalDateTime endDateTime,
            Customer user, CoworkingSpaceServiceImpl coworkingSpaceService,
            ReservationServiceImpl reservationService);
}
