package org.reservationapplication.ui;

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

import static org.reservationapplication.domain.util.UserInputHandler.*;
import static org.reservationapplication.ui.MenuConstants.*;

@Component
public class MenuController {

    private final MenuService menuService;
    private final CoworkingSpaceService coworkingSpaceService;
    private final ReservationService reservationService;

    @Autowired
    public MenuController(MenuService menuService,
                          CoworkingSpaceService coworkingSpaceService,
                          ReservationService reservationService) {
        this.menuService = menuService;
        this.coworkingSpaceService = coworkingSpaceService;
        this.reservationService = reservationService;
    }

    public void handleAddCoworkingSpace() {
        try {
            int typeChoice = intSupplierCreator.supplier(COWORKING_SPACE_TYPE_PROMPT).get();
            double price = doubleSupplierCreator.supplier(COWORKING_SPACE_PRICE_PROMPT).get();

            menuService.addCoworkingSpace(coworkingSpaceService, typeChoice, price);
            Loggers.USER_LOGGER.info("Coworking space added successfully.");
        } catch (IllegalArgumentException e) {
            Loggers.USER_LOGGER.warn("Error adding coworking space: " + e.getMessage());
            Loggers.TECHNICAL_LOGGER.error("Error in handleAddCoworkingSpace", e);
        }
    }


    public void handleRemoveCoworkingSpace() {
        try {
            long id = longSupplierCreator.supplier(COWORKING_SPACE_ID_PROMPT).get();
            menuService.removeCoworkingSpace(coworkingSpaceService, id);
            Loggers.USER_LOGGER.info("Coworking space removed successfully.");
        } catch (Exception e) {
            Loggers.USER_LOGGER.warn("Error removing coworking space: " + e.getMessage());
            Loggers.TECHNICAL_LOGGER.error("Error in handleRemoveCoworkingSpace", e);
        }
    }


    public void handleGetAllReservations() {
        List<Reservation> reservations = menuService.getAllReservations(reservationService);
        if (reservations != null && !reservations.isEmpty()) {
            for (Reservation reservation : reservations) {
                Loggers.USER_LOGGER.info(reservation.toString());
            }
        } else {
            Loggers.USER_LOGGER.warn("No reservations found.");
        }
    }

    public void handleGetAllCoworkingSpaces() {
        List<CoworkingSpace> spaces = menuService.getAllCoworkingSpaces(coworkingSpaceService);
        if (spaces != null && !spaces.isEmpty()) {
            for (CoworkingSpace space : spaces) {
                Loggers.USER_LOGGER.info(space.toString());
            }
        } else {
            Loggers.USER_LOGGER.warn("No coworking spaces found.");
        }
    }

    public void handleViewAvailableSpaces() {
        List<CoworkingSpace> spaces = menuService.getAvailableSpaces(coworkingSpaceService);
        if (spaces != null && !spaces.isEmpty()) {
            for (CoworkingSpace space : spaces) {
                Loggers.USER_LOGGER.info(space.toString());
            }
        } else {
            Loggers.USER_LOGGER.warn("No available coworking spaces found.");
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

            boolean success = menuService.makeReservation(user, coworkingSpaceService, reservationService,
                    coworkingSpaceID, reservationName, bookingDate, startTime, endTime);

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
            Loggers.USER_LOGGER.error("Invalid input: " + e.getMessage());
            Loggers.TECHNICAL_LOGGER.error("IllegalArgumentException in handleMakeReservation", e);
        }
    }

    public void handleCancelReservation() {
        try {
            long reservationId = longSupplierCreator.supplier(RESERVATION_ID_PROMPT).get();
            menuService.cancelReservation(reservationService, reservationId);
        } catch (Exception e) {
            Loggers.USER_LOGGER.error("Error cancelling reservation: " + e.getMessage());
            Loggers.TECHNICAL_LOGGER.error("Exception in handleCancelReservation", e);
        }
    }


    public void handleViewPersonalReservations(User user) {
        List<Reservation> reservations = menuService.viewPersonalReservations(user, reservationService);
        if (reservations != null && !reservations.isEmpty()) {
            for (Reservation reservation : reservations) {
                Loggers.USER_LOGGER.info(reservation.toString());
            }
        } else {
            Loggers.USER_LOGGER.warn("No personal reservations found.");
        }
    }
}