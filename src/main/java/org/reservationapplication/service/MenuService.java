package org.reservationapplication.service;

import org.reservationapplication.controller.UserChoiceCheckController;
import org.reservationapplication.logger.Loggers;
import org.reservationapplication.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import static org.reservationapplication.repository.CoworkingSpaceRepository.getNextID;

public class MenuService {
    UserChoiceCheckController userChoiceCheckController = new UserChoiceCheckController();
    Scanner scanner = new Scanner(System.in);

    public void addCoworkingSpace(CoworkingSpaceServiceImpl coworkingSpaceService) {
        CoworkingSpace coworkingSpace = new CoworkingSpace();
        coworkingSpace.setID(getNextID());

        Loggers.USER_LOGGER.info("Enter type of Coworking space");
        Loggers.USER_LOGGER.info("""
                1.Open space
                2.Private
                3.Room
                """);

        int choice = userChoiceCheckController.getUserChoiceInt();
        switch (choice){
            case 1:{
                coworkingSpace.setType(CoworkingSpaceType.OPENSPACE);
                break;
            }
            case 2:{
                coworkingSpace.setType(CoworkingSpaceType.PRIVATE);
                break;
            }
            case 3:{
                coworkingSpace.setType(CoworkingSpaceType.ROOM);
                break;
            }
            default:{
                Loggers.USER_LOGGER.warn("Invalid choice, please try again.");
                return;
            }
        }

        Loggers.USER_LOGGER.info("Enter price of Coworking space");
        double price = userChoiceCheckController.getUserChoiceDouble();
        coworkingSpace.setPrice(price);

        Loggers.USER_LOGGER.info("Enter availability status of Coworking space");
        Loggers.USER_LOGGER.info("""
                1.Available
                2.Unavailable
                """);
        choice = userChoiceCheckController.getUserChoiceInt();
        switch (choice){
            case 1:{
                coworkingSpace.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
                break;
            }
            case 2:{
                coworkingSpace.setAvailabilityStatus(AvailabilityStatus.UNAVAILABLE);
                break;
            }
            default:{
                Loggers.USER_LOGGER.warn("Invalid choice, please try again.");
                return;
            }
        }
        coworkingSpaceService.addCoworkingSpace(coworkingSpace);
    }

    public void removeCoworkingSpace(CoworkingSpaceServiceImpl coworkingSpaceService) {
        Loggers.USER_LOGGER.info("Enter id of a coworking space you want to be removed");
        long id = userChoiceCheckController.getUserChoiceLong();
        coworkingSpaceService.removeCoworkingSpace(id);
    }

    public void viewAllReservations(ReservationServiceImpl reservationService) {
        TreeSet<Reservation> reservations = reservationService.getAllReservation();;
        for (Reservation reservation : reservations) {
            System.out.println(reservation);
        }
    }

    public void viewAllCoworkingSpaces(CoworkingSpaceServiceImpl coworkingSpaceService){
        List<CoworkingSpace> coworkingSpaces = coworkingSpaceService.getAllCoworkingSpace();
        for (CoworkingSpace coworkingSpace : coworkingSpaces) {
            System.out.println(coworkingSpace);
        }
    }

    public void browseAvailableSpaces(CoworkingSpaceServiceImpl coworkingSpaceService) {
        List<CoworkingSpace> availableCoworkingSpaceList = coworkingSpaceService.loadAvailableCoworkingSpace();
        if (availableCoworkingSpaceList.isEmpty()){
            Loggers.USER_LOGGER.info("There are no available coworking spaces");
        }
        else {
            Loggers.USER_LOGGER.info("There are {} available coworking spaces", availableCoworkingSpaceList.size());
            System.out.println(availableCoworkingSpaceList);
        }
    }

    public void makeReservation(Customer user, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
        Loggers.USER_LOGGER.info("Enter id of coworking space you want to make");
        long coworkingSpaceID = userChoiceCheckController.getUserChoiceLong();

        Loggers.USER_LOGGER.info("Enter name for reservation");
        String reservationName = scanner.nextLine();

        Loggers.USER_LOGGER.info("Enter the reservation date (for example, 31.12.2025):");
        String dateInput = scanner.nextLine();

        Loggers.USER_LOGGER.info("Enter the start time of the reservation (for example, 10:00):");
        String startTimeInput = scanner.nextLine();

        Loggers.USER_LOGGER.info("Enter the end time of the reservation (for example, 12:00):");
        String endTimeInput = scanner.nextLine();

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
            System.out.println(e.getMessage());
            Loggers.USER_LOGGER.error("An illegal argument was provided. Please check your input.");
            Loggers.TECHNICAL_LOGGER.error("IllegalArgumentException occurred: {}", e.getMessage(), e);
        }
    }

    public void cancelReservation(ReservationServiceImpl reservationService) {
        Loggers.USER_LOGGER.info("Enter id of a reservation you want to be removed");
        long id = userChoiceCheckController.getUserChoiceLong();
        if (reservationService.removeReservationById(id)){
            Loggers.USER_LOGGER.info("Reservation has been cancelled");
            Loggers.TECHNICAL_LOGGER.info("Reservation with ID {} has been successfully deleted.", id);
        }
        else {
            Loggers.USER_LOGGER.info("Reservation with ID " + id + " not found.");
            Loggers.TECHNICAL_LOGGER.warn("Attempting to delete a non-existent reservation with an ID {}", id);
        }
    }

    public void viewPersonalReservations(User user, ReservationServiceImpl reservationService) {
        TreeSet<Reservation> personalReservations = reservationService.getPersonalReservation(user);
        if (!personalReservations.isEmpty()) {
            System.out.println(personalReservations);
        }
        else {
            Loggers.USER_LOGGER.info("There are no personal reservation list");
        }
    }
}
