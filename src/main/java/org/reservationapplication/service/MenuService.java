package org.reservationapplication.service;

import org.reservationapplication.logger.Loggers;
import org.reservationapplication.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import static org.reservationapplication.repository.CoworkingSpaceRepository.getNextID;
import static org.reservationapplication.service.Constants.*;
import static org.reservationapplication.util.UserInputHandler.*;

public class MenuService {

    public void addCoworkingSpace(CoworkingSpaceServiceImpl coworkingSpaceService) {
        CoworkingSpace coworkingSpace = new CoworkingSpace();
        coworkingSpace.setID(getNextID());

        int choice = intSupplierCreator.supplier(COWORKING_SPACE_TYPE_PROMPT).get();

        switch (choice){
            case 1:
                coworkingSpace.setType(CoworkingSpaceType.OPENSPACE);
                break;
            case 2:
                coworkingSpace.setType(CoworkingSpaceType.PRIVATE);
                break;
            case 3:
                coworkingSpace.setType(CoworkingSpaceType.ROOM);
                break;
            default:
                Loggers.USER_LOGGER.warn("Invalid choice, please try again.");
                return;
        }

        double price = doubleSupplierCreator.supplier(COWORKING_SPACE_PRICE_PROMPT).get();
        coworkingSpace.setPrice(price);

        Loggers.USER_LOGGER.info(COWORKING_SPACE_AVAILABILITY_PROMPT);
        choice = intSupplierCreator.supplier(COWORKING_SPACE_AVAILABILITY_STATUS_PROMPT).get();
        switch (choice){
            case 1:
                coworkingSpace.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
                break;
            case 2:
                coworkingSpace.setAvailabilityStatus(AvailabilityStatus.UNAVAILABLE);
                break;
            default:
                Loggers.USER_LOGGER.warn("Invalid choice, please try again.");
                return;
        }
        coworkingSpaceService.addCoworkingSpace(coworkingSpace);
    }

    public void removeCoworkingSpace(CoworkingSpaceServiceImpl coworkingSpaceService) {
        long id = longSupplierCreator.supplier(COWORKING_SPACE_ID_PROMPT).get();
        coworkingSpaceService.removeCoworkingSpace(id);
    }

    public void viewAllReservations(ReservationServiceImpl reservationService) {
        TreeSet<Reservation> reservations = reservationService.getAllReservation();
        if(!reservations.isEmpty()) {
            reservations.forEach(reservation -> Loggers.USER_LOGGER.info(reservation.toString()));
        }
        else {
            Loggers.USER_LOGGER.warn("No reservations found");
    }
    }

    public void viewAllCoworkingSpaces(CoworkingSpaceServiceImpl coworkingSpaceService){
        List<CoworkingSpace> coworkingSpaces = coworkingSpaceService.getAllCoworkingSpace();
        if(!coworkingSpaces.isEmpty()) {
            coworkingSpaces.forEach(coworkingSpace -> Loggers.USER_LOGGER.info(coworkingSpace.toString()));
        }
        else {
            Loggers.USER_LOGGER.warn("No coworking space found");
        }
    }

    public void viewAvailableSpaces(CoworkingSpaceServiceImpl coworkingSpaceService) {
        List<CoworkingSpace> availableCoworkingSpaces = coworkingSpaceService.getAvailableCoworkingSpace();
        if(!availableCoworkingSpaces.isEmpty()) {
            availableCoworkingSpaces.forEach(reservation -> Loggers.USER_LOGGER.info(availableCoworkingSpaces.toString()));
        }
        else {
            Loggers.USER_LOGGER.warn("No coworking space found");
        }
    }

    public void makeReservation(Customer user, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
        long coworkingSpaceID = longSupplierCreator.supplier(COWORKING_SPACE_ID_PROMPT).get();

        String reservationName = stringSupplierCreator.supplier(RESERVATION_NAME_PROMPT).get();

        String dateInput = stringSupplierCreator.supplier(RESERVATION_DATE_PROMPT).get();

        String startTimeInput = stringSupplierCreator.supplier(START_TIME_PROMPT).get();

        String endTimeInput = stringSupplierCreator.supplier(END_TIME_PROMPT).get();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        try {
            LocalDate bookingDate = LocalDate.parse(dateInput, dateFormatter);
            LocalTime startTime = LocalTime.parse(startTimeInput, timeFormatter);
            LocalTime endTime = LocalTime.parse(endTimeInput, timeFormatter);


            LocalDateTime startDateTime = LocalDateTime.of(bookingDate, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(bookingDate, endTime);

            if(reservationService.userAddReservation(coworkingSpaceID, reservationName, bookingDate, startDateTime, endDateTime, user, coworkingSpaceService, reservationService)) {
                Loggers.USER_LOGGER.info("Reservation added successfully");
                Loggers.TECHNICAL_LOGGER.info("Reservation of coworking space {} added successfully", coworkingSpaceID);
            }
            else {
                Loggers.USER_LOGGER.info("Space is unavailable");
            }

        } catch (DateTimeParseException e) {
            Loggers.USER_LOGGER.error("Invalid date or time format. Please try again.");
            Loggers.TECHNICAL_LOGGER.error("DateTimeParseException occurred: Invalid date or time format.", e);
        } catch (IllegalArgumentException e) {
            Loggers.USER_LOGGER.error("An illegal argument was provided. Please check your input.");
            Loggers.TECHNICAL_LOGGER.error("IllegalArgumentException occurred: {}", e.getMessage(), e);
        }
    }

    public void cancelReservation(ReservationServiceImpl reservationService) {
        long id = longSupplierCreator.supplier(RESERVATION_ID_PROMPT).get();
        if (reservationService.removeReservationById(id)){
            Loggers.USER_LOGGER.info("Reservation has been cancelled");
            Loggers.TECHNICAL_LOGGER.info("Reservation with ID {} has been successfully deleted.", id);
        }
        else {
            Loggers.USER_LOGGER.warn("Reservation with ID " + id + " not found.");
            Loggers.TECHNICAL_LOGGER.warn("Attempting to delete a non-existent reservation with an ID {}", id);
        }
    }

    public void viewPersonalReservations(User user, ReservationServiceImpl reservationService) {
        TreeSet<Reservation> personalReservations = reservationService.getPersonalReservation(user);
        if(!personalReservations.isEmpty()) {
            personalReservations.forEach(coworkingSpace -> Loggers.USER_LOGGER.info(coworkingSpace.toString()));
        }
        else {
            Loggers.USER_LOGGER.warn("No reservations found");
        }   }
}
