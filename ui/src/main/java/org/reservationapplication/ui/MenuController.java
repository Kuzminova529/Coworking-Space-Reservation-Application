package org.reservationapplication.ui;

import org.reservationapplication.domain.exeption.BusinessException;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.model.User;
import org.reservationapplication.logger.Loggers;
import org.reservationapplication.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.time.LocalDateTime;

import static org.reservationapplication.domain.util.UserInputHandler.*;
import static org.reservationapplication.ui.MenuConstants.*;

@Component
public class MenuController {

    private final CoworkingSpaceService coworkingSpaceService;
    private final ReservationService reservationService;

    @Autowired
    public MenuController(CoworkingSpaceService coworkingSpaceService, ReservationService reservationService) {
        this.coworkingSpaceService = coworkingSpaceService;
        this.reservationService = reservationService;
    }

    public void handleAddCoworkingSpace() {
        try {
            int typeChoice = intSupplierCreator.supplier(COWORKING_SPACE_TYPE_PROMPT).get();
            double price = doubleSupplierCreator.supplier(COWORKING_SPACE_PRICE_PROMPT).get();

            coworkingSpaceService.addCoworkingSpace(typeChoice, price);
            Loggers.USER_LOGGER.info("Coworking space added successfully.");
        } catch (IllegalArgumentException e) {
            Loggers.USER_LOGGER.warn("Error adding coworking space");
            Loggers.TECHNICAL_LOGGER.error("Error in handleAddCoworkingSpace", e);
        } catch (BusinessException e) {
            Loggers.USER_LOGGER.warn("Error adding coworking space");
        }
    }


    public void handleRemoveCoworkingSpace() {
        try {
            long id = longSupplierCreator.supplier(COWORKING_SPACE_ID_PROMPT).get();
            coworkingSpaceService.removeCoworkingSpace(id);
            Loggers.USER_LOGGER.info("Coworking space removed successfully.");
        } catch (BusinessException e) {
            Loggers.USER_LOGGER.warn("Error removing coworking space");
            Loggers.TECHNICAL_LOGGER.error("Error in handleRemoveCoworkingSpace", e);
        }
    }


    public void handleGetAllReservations() {
        try {
            List<Reservation> reservations = reservationService.getAllReservation();
            if (reservations != null && !reservations.isEmpty()) {
                for (Reservation reservation : reservations) {
                    Loggers.USER_LOGGER.info(reservation.toString());
                }
            } else {
                Loggers.USER_LOGGER.warn("No reservations found.");
            }
        } catch (BusinessException e) {
            Loggers.USER_LOGGER.warn("Error retrieving all reservations");
        }
    }

    public void handleGetAllCoworkingSpaces() {
        try {
            List<CoworkingSpace> spaces = coworkingSpaceService.getAllCoworkingSpace();
            if (spaces != null && !spaces.isEmpty()) {
                for (CoworkingSpace space : spaces) {
                    Loggers.USER_LOGGER.info(space.toString());
                }
            } else {
                Loggers.USER_LOGGER.warn("No coworking spaces found.");
            }
        } catch (BusinessException e) {
            Loggers.USER_LOGGER.warn("Error retrieving all coworking spaces");
        }
    }

    public void handleGetActiveSpaces() {
        try {
            List<CoworkingSpace> spaces = coworkingSpaceService.getActiveCoworkingSpace();
            if (spaces != null && !spaces.isEmpty()) {
                for (CoworkingSpace space : spaces) {
                    Loggers.USER_LOGGER.info(space.toString());
                }
            } else {
                Loggers.USER_LOGGER.warn("No available coworking spaces found.");
            }
        } catch (BusinessException e) {
            Loggers.USER_LOGGER.warn("Error retrieving all coworking spaces");
        }
    }

    public void handleMakeReservation(User user) {
        try {
            long coworkingSpaceID = longSupplierCreator.supplier(COWORKING_SPACE_ID_PROMPT).get();
            String reservationName = stringSupplierCreator.supplier(RESERVATION_NAME_PROMPT).get();
            String dateInput = stringSupplierCreator.supplier(RESERVATION_DATE_PROMPT).get();
            String startTimeInput = stringSupplierCreator.supplier(START_TIME_PROMPT).get();
            String endTimeInput = stringSupplierCreator.supplier(END_TIME_PROMPT).get();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            LocalDate bookingDate = LocalDate.parse(dateInput, dateFormatter);
            LocalTime startTime = LocalTime.parse(startTimeInput, timeFormatter);
            LocalTime endTime = LocalTime.parse(endTimeInput, timeFormatter);

            LocalDateTime startDateTime = LocalDateTime.of(bookingDate, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(bookingDate, endTime);

            boolean success = reservationService.userAddReservation(coworkingSpaceID, reservationName,
                    bookingDate, startDateTime, endDateTime, user, coworkingSpaceService);

            if (success) {
                Loggers.USER_LOGGER.info("Reservation added successfully.");
                Loggers.TECHNICAL_LOGGER.info("Reservation for coworking space {} added successfully.", coworkingSpaceID);
            } else {
                Loggers.USER_LOGGER.info("Space is unavailable for reservation.");
            }
        } catch (DateTimeParseException e) {
            Loggers.USER_LOGGER.error("Invalid date or time format. Please try again.");
            Loggers.TECHNICAL_LOGGER.error("DateTimeParseException in handleMakeReservation", e);
        } catch (IllegalArgumentException e) {
            Loggers.USER_LOGGER.error("Invalid input");
            Loggers.TECHNICAL_LOGGER.error("IllegalArgumentException in handleMakeReservation", e);
        } catch (BusinessException e){
            Loggers.USER_LOGGER.error("Error while creating reservation");

        }
    }

    public void handleCancelReservation() {
        try {
            long reservationId = longSupplierCreator.supplier(RESERVATION_ID_PROMPT).get();
            reservationService.removeReservationById(reservationId);
        } catch (BusinessException e) {
            Loggers.USER_LOGGER.error("Error cancelling reservation");
            Loggers.TECHNICAL_LOGGER.error("Exception in handleCancelReservation", e);
        }
    }


    public void handleViewPersonalReservations(User user) {
        try {
            List<Reservation> reservations = reservationService.getPersonalReservation(user);
            if (reservations != null && !reservations.isEmpty()) {
                for (Reservation reservation : reservations) {
                    Loggers.USER_LOGGER.info(reservation.toString());
                }
            } else {
                Loggers.USER_LOGGER.warn("No personal reservations found.");
            }
        } catch (BusinessException e) {
            Loggers.USER_LOGGER.warn("Error retrieving personal reservations");
        }
    }
}