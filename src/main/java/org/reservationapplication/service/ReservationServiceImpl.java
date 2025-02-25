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

public class ReservationServiceImpl implements ReservationService{
    private final List<Reservation> allReservation;


    public ReservationServiceImpl() {
        this.allReservation = new ArrayList<>();
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

    public void removeReservationById(long id) {
        Iterator<Reservation> iterator = allReservation.iterator();
        while (iterator.hasNext()) {
            Reservation reservation = iterator.next();
            if (reservation.getReservationID() == id) {
                iterator.remove();
                System.out.println("Reservation with ID " + id + " has been removed.");
            }
        }
        System.out.println("Reservation with ID " + id + " not found.");
    }

    public void addReservation(Reservation reservation) {
        allReservation.add(reservation);
    }

    public void userAddReservation(
            long id, String reservationName, String dateInput,
            String startTimeInput, String endTimeInput,
            Customer user, CoworkingSpaceServiceImpl coworkingSpaceService,
            ReservationServiceImpl reservationService) {

        if (coworkingSpaceService.getAllCoworkingSpace()
                .stream()
                .anyMatch(coworkingSpace -> coworkingSpace.getID() == id && coworkingSpace.getAvailabilityStatus()== AvailabilityStatus.AVAILABLE)) {
            Reservation reservation = new Reservation();
            reservation.setCoworkingSpaceID(id);
            reservation.setCustomerID(user.getId());
            reservation.setReservationName(reservationName);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            try {
                LocalDate bookingDate = LocalDate.parse(dateInput, dateFormatter);
                LocalTime startTime = LocalTime.parse(startTimeInput, timeFormatter);
                LocalTime endTime = LocalTime.parse(endTimeInput, timeFormatter);

                LocalDate today = LocalDate.now();

                if (bookingDate.isBefore(today)) {
                    throw new IllegalArgumentException("You cannot register a past date!");
                }

                LocalDateTime startDateTime = LocalDateTime.of(bookingDate, startTime);
                LocalDateTime endDateTime = LocalDateTime.of(bookingDate, endTime);

                if (!startDateTime.isBefore(endDateTime)) {
                    throw new IllegalArgumentException("The reservation start time must be before the end time!");
                }

                reservation.setStartDateTime(startDateTime);
                reservation.setEndDateTime(endDateTime);

                reservationService.addReservation(reservation);

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date or time format. Try again.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid ID");
        }
    }
}