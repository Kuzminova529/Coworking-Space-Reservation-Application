package org.reservationapplication.model;

import org.reservationapplication.logger.Loggers;
import org.reservationapplication.repository.ApplicationStateRepository;
import org.reservationapplication.service.CoworkingSpaceServiceImpl;
import org.reservationapplication.service.MenuService;
import org.reservationapplication.service.ReservationServiceImpl;
import org.reservationapplication.controller.MenuController;

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
        MenuController menuController = new MenuController(new MenuService(), coworkingSpaceService, reservationService);
        while (true) {
            int choice = intSupplierCreator.supplier(ADMIN_MENU_PROMPT).get();
            switch (choice) {
                case 1: {
                    menuController.handleAddCoworkingSpace();
                    ApplicationStateRepository appState = new ApplicationStateRepository(user, coworkingSpaceService, reservationService);
                    appState.saveState();
                    break;
                }
                case 2: {
                    menuController.handleRemoveCoworkingSpace();
                    ApplicationStateRepository appState = new ApplicationStateRepository(user, coworkingSpaceService, reservationService);
                    appState.saveState();
                    break;
                }
                case 3: {
                    menuController.handleViewAllReservations();
                    break;
                }
                case 4: {
                    menuController.handleViewAllCoworkingSpaces();
                    break;
                }
                case 5: {
                    return;
                }
                default: {
                    Loggers.USER_LOGGER.warn("Invalid choice, please try again.");
                }
            }
        }
    }

    public void customerMenu(Customer user, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
        MenuController menuController = new MenuController(new MenuService(), coworkingSpaceService, reservationService);
        while (true) {
            int choice = intSupplierCreator.supplier(CUSTOMER_MENU_PROMPT).get();
            switch (choice) {
                case 1: {
                    menuController.handleViewAvailableSpaces();
                    break;
                }
                case 2: {
                    menuController.handleMakeReservation(user);
                    ApplicationStateRepository appState= new ApplicationStateRepository(user, coworkingSpaceService, reservationService);
                    appState.saveState();
                    break;
                }
                case 3:{
                    menuController.handleCancelReservation();
                    ApplicationStateRepository appState= new ApplicationStateRepository(user, coworkingSpaceService, reservationService);
                    appState.saveState();
                    break;
                }
                case 4:{
                    menuController.handleViewPersonalReservations(user);
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
