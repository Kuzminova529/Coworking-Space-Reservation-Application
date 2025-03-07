package org.reservationapplication.model;

import org.reservationapplication.logger.Loggers;
import org.reservationapplication.repository.ApplicationStateRepository;
import org.reservationapplication.service.CoworkingSpaceServiceImpl;
import org.reservationapplication.service.MenuService;
import org.reservationapplication.service.ReservationServiceImpl;

import static org.reservationapplication.service.Constants.*;
import static org.reservationapplication.util.UserInputHandler.intSupplierCreator;


public class Menu {

    public void welcomeMenu(User user, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
        Loggers.USER_LOGGER.info("Welcome to Reservation Application");
        mainMenu(user, coworkingSpaceService, reservationService);
    }

    public void mainMenu(User user, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
        while (true) {
             int choice = intSupplierCreator.supplier(MAIN_MENU_PROMPT).get();

            switch (choice) {
                case 1:
                    if ((user instanceof Admin)) {
                        adminMenu(user, coworkingSpaceService, reservationService);
                    } else {
                        Loggers.USER_LOGGER.warn("You are not an admin. Choose another option");
                    }
                    break;
                case 2:
                    if (user instanceof Customer) {
                        customerMenu((Customer) user, coworkingSpaceService, reservationService);
                    } else {
                        Loggers.USER_LOGGER.warn("You are not a customer. Choose another option");
                    }
                    break;
                case 3:
                    Loggers.USER_LOGGER.info("Exiting...");
                    return;
                default:
                    Loggers.USER_LOGGER.warn("Invalid choice, please try again.(1-3)");
            }
        }
    }

    public void adminMenu( User user, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
        MenuService menuService = new MenuService();
        while (true) {
            int choice = intSupplierCreator.supplier(ADMIN_MENU_PROMPT).get();
            switch (choice) {
                case 1: {
                    menuService.addCoworkingSpace(coworkingSpaceService);
                    ApplicationStateRepository appState = new ApplicationStateRepository(user, coworkingSpaceService, reservationService);
                    appState.saveState();
                    break;
                }
                case 2: {
                    menuService.removeCoworkingSpace(coworkingSpaceService);
                    ApplicationStateRepository appState = new ApplicationStateRepository(user, coworkingSpaceService, reservationService);
                    appState.saveState();
                    break;
                }
                case 3: {
                    menuService.viewAllReservations(reservationService);
                    break;
                }
                case 4: {
                    menuService.viewAllCoworkingSpaces(coworkingSpaceService);
                    break;
                }
                case 5: {
                    Loggers.USER_LOGGER.info("Exiting...");
                    return;
                }
                default: {
                    Loggers.USER_LOGGER.warn("Invalid choice, please try again.");
                }
            }
        }
    }

    public void customerMenu(Customer user, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
        MenuService menuService = new MenuService();
        while (true) {
            int choice = intSupplierCreator.supplier(CUSTOMER_MENU_PROMPT).get();
            switch (choice) {
                case 1: {
                    menuService.viewAvailableSpaces(coworkingSpaceService);
                    break;
                }
                case 2: {
                    menuService.makeReservation(user, coworkingSpaceService, reservationService);
                    ApplicationStateRepository appState= new ApplicationStateRepository(user, coworkingSpaceService, reservationService);
                    appState.saveState();
                    break;
                }
                case 3:{
                    menuService.cancelReservation(reservationService);
                    ApplicationStateRepository appState= new ApplicationStateRepository(user, coworkingSpaceService, reservationService);
                    appState.saveState();
                    break;
                }
                case 4:{
                    menuService.viewPersonalReservations(user, reservationService);
                    break;
                }
                case 5: {
                    return;
                }
                default: {
                    Loggers.USER_LOGGER.warn("Invalid choice, please try again.(1-5)");
                }
            }
        }
    }
}
