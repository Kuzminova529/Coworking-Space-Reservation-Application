package org.reservationapplication.service;

import org.reservationapplication.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import static org.reservationapplication.controller.UserChoiceCheckController.*;
import static org.reservationapplication.repository.CoworkingSpaceRepository.getNextID;

public class MenuService {

    Scanner scanner = new Scanner(System.in);

    public void addCoworkingSpace(CoworkingSpaceServiceImpl coworkingSpaceService) {
        CoworkingSpace coworkingSpace = new CoworkingSpace();
        coworkingSpace.setID(getNextID());

        System.out.println("Enter type of Coworking space");
        System.out.print("""
                1.Open space
                2.Private
                3.Room
                """);

        int choice = getUserChoiceInt();
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
                System.out.println("Invalid choice, please try again.");
                return;
            }
        }

        System.out.println("Enter price of Coworking space");
        double price = getUserChoiceDouble();
        coworkingSpace.setPrice(price);

        System.out.println("Enter availability status of Coworking space");
        System.out.print("""
                1.Available
                2.Unavailable
                """);
        choice = getUserChoiceInt();
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
                System.out.println("Invalid choice, please try again.");
                return;
            }
        }
        coworkingSpaceService.addCoworkingSpace(coworkingSpace);
    }

    public void removeCoworkingSpace(CoworkingSpaceServiceImpl coworkingSpaceService) {
        System.out.println("Enter id of a coworking space you want to be removed");
        long id = getUserChoiceLong();
        coworkingSpaceService.removeCoworkingSpace(id);
    }

    public void viewAllReservations(ReservationServiceImpl reservationService) {
        TreeSet<Reservation> reservations = reservationService.getAllReservation();;
        for (Reservation reservation : reservations) {
            System.out.println(reservation);
        }    }

    public void viewAllCoworkingSpaces(CoworkingSpaceServiceImpl coworkingSpaceService){
        List<CoworkingSpace> coworkingSpaces = coworkingSpaceService.getAllCoworkingSpace();
        for (CoworkingSpace coworkingSpace : coworkingSpaces) {
            System.out.println(coworkingSpace);
        }
    }

    public void browseAvailableSpaces(CoworkingSpaceServiceImpl coworkingSpaceService) {
        List<CoworkingSpace> availableCoworkingSpaceList = coworkingSpaceService.loadAvailableCoworkingSpace();
        if (availableCoworkingSpaceList.isEmpty()){
            System.out.println("There are no available coworking spaces");
        }
        else {
            System.out.println("There are " + availableCoworkingSpaceList.size() + " available coworking spaces");
            System.out.println(availableCoworkingSpaceList);
        }
    }

    public void makeReservation(Customer user, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
        System.out.println("Enter id of coworking space you want to make");
        long coworkingSpaceID = getUserChoiceLong();

        System.out.println("Enter name for reservation");
        String reservationName = scanner.nextLine();

        System.out.println("Enter the reservation date (for example, 31.12.2025):");
        String dateInput = scanner.nextLine();

        System.out.println("Enter the start time of the reservation (for example, 10:00):");
        String startTimeInput = scanner.nextLine();

        System.out.println("Enter the end time of the reservation (for example, 12:00):");
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
                System.out.println("Reservation added successfully");
            }
            else {
                System.out.println("Space is unavailable");
            }

        } catch (DateTimeParseException e) {
        System.out.println("Invalid date or time format. Try again.");
        } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
        }
    }

    public void cancelReservation(ReservationServiceImpl reservationService) {
        System.out.println("Enter id of a reservation you want to be removed");
        long id = getUserChoiceLong();
        if (reservationService.removeReservationById(id)){
            System.out.println("Reservation has been cancelled");
        }
        else {
            System.out.println("Reservation with ID " + id + " not found.");
        }
    }

    public void viewPersonalReservations(User user, ReservationServiceImpl reservationService) {
        TreeSet<Reservation> personalReservations =reservationService.getPersonalReservation(user);
        if (!personalReservations.isEmpty()) {
            System.out.println(personalReservations);
        }
        else {
            System.out.println("There are no personal reservation list");
        }
    }
}
