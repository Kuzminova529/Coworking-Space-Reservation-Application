package org.reservationapplication;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Iterator;
import java.util.List;

public class ReservationService {
    private final List<Reservation> personalReservation;
    private final List<Reservation> generalReservationList;


    public ReservationService(List<Reservation> personalReservation, List<Reservation> generalReservationList) {
        this.personalReservation = personalReservation;
        this.generalReservationList = generalReservationList;
    }

    public List<Reservation> getPersonalReservation() {
        return personalReservation;
    }

    public boolean removePersonalReservationById(long id) {
        Iterator<Reservation> iterator = personalReservation.iterator();
        while (iterator.hasNext()) {
            Reservation reservation = iterator.next();
            if (reservation.getReservationID() == id) {
                iterator.remove();
                System.out.println("Reservation with ID " + id + " has been removed.");
                return true;
            }
        }
        System.out.println("Reservation with ID " + id + " not found.");
        return false;
    }

    public void addPersonalReservation(Reservation reservation) {
        personalReservation.add(reservation);
    }

    public void addGeneralReservation(Reservation reservation) {
        generalReservationList.add(reservation);
    }

    public void removeFromGeneralReservationById(long id) {
        Iterator<Reservation> iterator = generalReservationList.iterator();
        while (iterator.hasNext()) {
            Reservation reservation = iterator.next();
            if (reservation.getReservationID()==id) {
                iterator.remove();
                return;
            }
        }
        System.out.println("Reservation with ID " + id + " not found.");
    }

    public boolean isAddedReservation(
            long id, String reservationName, String dateInput,
            String startTimeInput, String endTimeInput,
            Customer user, CoworkingSpaceService coworkingSpaceService,
            ReservationService reservationService) {

        if (coworkingSpaceService.isIDMatch(id)) {
            Reservation reservation = new Reservation(reservationService);
            reservationService.addPersonalReservation(reservation);
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

                reservation.setStartReservationDateAndTime(startDateTime);
                reservation.setEndReservationDateAndTime(endDateTime);

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date or time format. Try again.");
                return false;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return false;
            }
            return true;
        } else {
            System.out.println("Invalid ID");
            return false;
        }
    }


    public void printGeneralReservation() {
        for (Reservation reservation : generalReservationList) {
            System.out.println(reservation);
        }
    }
}
