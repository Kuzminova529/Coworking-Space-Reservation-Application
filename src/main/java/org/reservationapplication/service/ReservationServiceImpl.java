package org.reservationapplication.service;

import org.reservationapplication.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationServiceImpl implements ReservationService{
    private final List<Reservation> allReservation;


    public ReservationServiceImpl() {
        this.allReservation = new ArrayList<>();
    }

    public ReservationServiceImpl(List<Reservation> allReservation) {
        this.allReservation = allReservation;
    }

    public List<Reservation> getAllReservation() {
        return allReservation;
    }

    public List<Reservation> getPersonalReservation(User user) {
        List<Reservation> reservations = getAllReservation();
        List<Reservation> personalReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getCustomerID() == user.getId()) {
                personalReservations.add(reservation);
            }
        }
        return reservations;
    }

    public List<Reservation> getReservationsByCoworkingSpaceAndDate(long coworkingSpaceId, LocalDate date) {
        return allReservation.stream()
                .filter(r -> r.getCoworkingSpaceID() == coworkingSpaceId && r.getStartDateTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    public boolean removeReservationById(long id) {
        Iterator<Reservation> iterator = allReservation.iterator();
        while (iterator.hasNext()) {
            Reservation reservation = iterator.next();
            if (reservation.getReservationID() == id) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public void addReservation(Reservation reservation) {
        allReservation.add(reservation);
    }

    public boolean userAddReservation(
            long id, String reservationName, LocalDate bookingDate,
            LocalDateTime startDateTime, LocalDateTime endDateTime,
            Customer user, CoworkingSpaceServiceImpl coworkingSpaceService,
            ReservationServiceImpl reservationService) {

        if (coworkingSpaceService.getCoworkingSpaceByID(id).getAvailabilityStatus() == AvailabilityStatus.AVAILABLE ) {
            Reservation reservation = new Reservation();
            reservation.setCoworkingSpaceID(id);
            reservation.setCustomerID(user.getId());
            reservation.setReservationName(reservationName);

            LocalDate today = LocalDate.now();

                if (bookingDate.isBefore(today)) {
                    throw new IllegalArgumentException("You cannot register a past date!");
                }

                if (!startDateTime.isBefore(endDateTime)) {
                    throw new IllegalArgumentException("The reservation start time must be before the end time!");
                }

                List<Reservation> existingReservations = reservationService.getReservationsByCoworkingSpaceAndDate(id, bookingDate);

                for (Reservation existing : existingReservations) {

                    LocalDateTime existingStart = existing.getStartDateTime();
                    LocalDateTime existingEnd = existing.getEndDateTime();

                    // The exact same time
                    if (startDateTime.equals(existingStart) && endDateTime.equals(existingEnd)) {
                        throw new IllegalArgumentException("This exact time slot is already booked!");
                    }

                    // Intersection of time
                    if (startDateTime.isBefore(existingEnd) && endDateTime.isAfter(existingStart)) {
                        throw new IllegalArgumentException("This time slot is already booked!");
                    }
                }

                reservation.setStartDateTime(startDateTime);
                reservation.setEndDateTime(endDateTime);

                reservationService.addReservation(reservation);

                return true;
        }
        return false;
    }
}