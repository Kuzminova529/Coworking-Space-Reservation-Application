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
import static org.reservationapplication.util.InputSupplierFactory.*;

public class MenuService {

    public void addCoworkingSpace(CoworkingSpaceServiceImpl coworkingSpaceService) {
        CoworkingSpace coworkingSpace = new CoworkingSpace();
        coworkingSpace.setID(getNextID());

        int choice = intSupplierCreator.supplier("""
                Enter type of Coworking space:
                1.Open space
                2.Private
                3.Room
                """).get();

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

        double price = doubleSupplierCreator.supplier("Enter price of Coworking space").get();
        coworkingSpace.setPrice(price);

        Loggers.USER_LOGGER.info("Enter availability status of Coworking space");
        choice = intSupplierCreator.supplier("""
                1.Available
                2.Unavailable
                """).get();
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
        long id = longSupplierCreator.supplier("Enter id of a coworking space you want to be removed").get();
        coworkingSpaceService.removeCoworkingSpace(id);
    }

    public void viewAllReservations(ReservationServiceImpl reservationService) {
        Optional<TreeSet<Reservation>> reservations = reservationService.getAllReservation();
        reservations.ifPresentOrElse(
                list -> list.forEach(reservation -> Loggers.USER_LOGGER.info(reservation.toString())),
                () -> Loggers.USER_LOGGER.warn("No reservations found")
        );
    }

    public void viewAllCoworkingSpaces(CoworkingSpaceServiceImpl coworkingSpaceService){
        Optional<List<CoworkingSpace>> optionalSpaces = coworkingSpaceService.getAllCoworkingSpace();
        optionalSpaces.ifPresentOrElse(
                list -> list.forEach(coworkingSpace -> Loggers.USER_LOGGER.info(coworkingSpace.toString())),
                () -> Loggers.USER_LOGGER.warn("No soworking spaces found"));
    }

    public void viewAvailableSpaces(CoworkingSpaceServiceImpl coworkingSpaceService) {
        Optional<List<CoworkingSpace>> availableCoworkingSpaceList = coworkingSpaceService.getAvailableCoworkingSpace();
        availableCoworkingSpaceList.ifPresentOrElse(
                list -> list.forEach(availableCoworkingSpace -> Loggers.USER_LOGGER.info(availableCoworkingSpace.toString())),
                () -> Loggers.USER_LOGGER.warn("There are no available coworking spaces"));
    }

    public void makeReservation(Customer user, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
        long coworkingSpaceID = longSupplierCreator.supplier("Enter id of coworking space you want to make").get();

        String reservationName = stringSupplierCreator.supplier("Enter name for reservation").get();

        String dateInput = stringSupplierCreator.supplier("Enter the reservation date (for example, 31.12.2025):").get();

        String startTimeInput = stringSupplierCreator.supplier("Enter the start time (for example, 10:00):").get();

        String endTimeInput = stringSupplierCreator.supplier("Enter the end time (for example, 12:00):").get();

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
        long id = longSupplierCreator.supplier("Enter id of a reservation you want to be removed").get();
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
        Optional<TreeSet<Reservation>> personalReservations = reservationService.getPersonalReservation(user);
        personalReservations.ifPresentOrElse(
                list -> list.forEach(personalReservation -> Loggers.USER_LOGGER.info(personalReservation.toString())),
                () -> Loggers.USER_LOGGER.warn("There are no personal reservations"));
    }
}
