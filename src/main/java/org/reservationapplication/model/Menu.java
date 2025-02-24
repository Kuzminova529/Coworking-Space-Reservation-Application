package org.reservationapplication.model;

import org.reservationapplication.service.CoworkingSpaceServiceImpl;
import org.reservationapplication.service.MenuService;
import org.reservationapplication.service.ReservationServiceImpl;
import org.reservationapplication.controller.UserChoiceCheckController;

public class Menu {

    public void welcomeMenu(User user, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
        System.out.println("Welcome to Reservation Application");
        mainMenu(user, coworkingSpaceService, reservationService);
    }


    public void mainMenu(User user, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
        while (true) {
            System.out.println("""
                    1. Admin menu
                    2. Customer menu
                    3. Exit
                    """);
            int choice = UserChoiceCheckController.getUserChoiceInt();
            switch (choice) {
                case 1:
                    if ((user instanceof Admin)) {
                        adminMenu(coworkingSpaceService, reservationService);
                    } else {
                        System.out.println("You are not an admin. Choose another option");
                    }
                    break;
                case 2:
                    if (user instanceof Customer) {
                        customerMenu((Customer) user, coworkingSpaceService, reservationService);
                    } else {
                        System.out.println("You are not a customer. Choose another option");
                    }
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    public void adminMenu(CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
        MenuService menuService = new MenuService();
        while (true) {
            System.out.println("""
                    1. Add a new coworking space
                    2. Remove a coworking space
                    3. View all reservations
                    4. View all coworking spaces
                    5. Back to Main Menu
                    """);
            int choice = UserChoiceCheckController.getUserChoiceInt();
            switch (choice) {
                case 1:
                    menuService.addCoworkingSpace(coworkingSpaceService);
                    break;
                case 2:
                    menuService.removeCoworkingSpace(coworkingSpaceService);
                    break;
                case 3:
                    menuService.viewAllReservations(reservationService);
                    break;
                case 4:
                    menuService.viewAllCoworkingSpaces(coworkingSpaceService);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }


    public void customerMenu(Customer user, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
        MenuService menuService = new MenuService();
        while (true) {
            System.out.println("""
                    1. Browse available spaces
                    2. Make a reservation
                    3. Cancel reservation
                    4. View my reservations
                    5. Back to Main Menu
                    """);
            int choice = UserChoiceCheckController.getUserChoiceInt();
            switch (choice) {
                case 1: {
                    menuService.browseAvailableSpaces(coworkingSpaceService);
                    break;
                }
                case 2: {
                    menuService.makeReservation(user, coworkingSpaceService, reservationService);
                }
                case 3: {
                    menuService.cancelReservation(reservationService);
                    break;
                }
                case 4: {
                    menuService.viewPersonalReservations(reservationService);
                    break;
                }
                case 5: {
                    return;
                }
                default: {

                    System.out.println("Invalid choice, please try again.");
                }
            }
        }
    }
}
