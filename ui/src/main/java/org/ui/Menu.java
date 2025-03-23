package org.ui;

import org.domain.model.Admin;
import org.domain.model.Customer;
import org.domain.model.User;
import org.logger.Loggers;
import org.service.CoworkingSpaceServiceImpl;
import org.service.MenuService;
import org.service.ReservationServiceImpl;
import org.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.ui.MenuConstants.*;
import static org.domain.util.UserInputHandler.intSupplierCreator;
import static org.domain.util.UserInputHandler.longSupplierCreator;


@Component
public class Menu {
    private final UserService userService;
    private final CoworkingSpaceServiceImpl coworkingSpaceService;
    private final ReservationServiceImpl reservationService;
    private final MenuController menuController;

    @Autowired
    public Menu(UserService userService, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService, MenuController menuController) {
        this.userService = userService;
        this.coworkingSpaceService = coworkingSpaceService;
        this.reservationService = reservationService;
        this.menuController = menuController;
    }

    public void welcomeMenu() {
        Loggers.USER_LOGGER.info("Welcome to Reservation Application");
        userMenu(userService, coworkingSpaceService, reservationService);
    }

    private void userMenu(UserService userService, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
        int choice = intSupplierCreator.supplier("Do you have an account?\n1.YES\n2.NO").get();
        while (true){
            switch (choice) {
                case 1: {
                    long id = longSupplierCreator.supplier("Enter your id").get();
                    Optional<User> optUser = userService.findUserByID(id);
                    if (optUser.isPresent()) {
                        User user = optUser.get();
                        if (user instanceof Admin) {
                            adminMenu(user, coworkingSpaceService, reservationService);
                        }
                        else {
                            customerMenu(user, coworkingSpaceService, reservationService);
                        }
                    } else {
                        Loggers.USER_LOGGER.warn("User not found, please sign up");
                        return;
                    }
                    break;
                }
                case 2: {
                    int roleAns = intSupplierCreator.supplier("Who are you?\n1.Admin\n2.Customer").get();
                    switch (roleAns) {
                        case 1:
                            User admin = new Admin();
                            userService.createUser(admin);
                            adminMenu(admin, coworkingSpaceService, reservationService);
                            break;
                        case 2:
                            User customer = new Customer();
                            userService.createUser(customer);
                            customerMenu(customer, coworkingSpaceService, reservationService);
                            break;
                        default:
                            Loggers.USER_LOGGER.warn("Invalid choice, please try again.(1-2)");
                            break;
                    }
                }
                default:
                    Loggers.USER_LOGGER.warn("Invalid choice, please try again.(1-2)");
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
                    break;
                }
                case 2: {
                    menuController.handleRemoveCoworkingSpace();
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
                    System.exit(0);
                }
                default: {
                    Loggers.USER_LOGGER.warn("Invalid choice, please try again.");
                }
            }
        }
    }

    public void customerMenu(User user, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
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
                    break;
                }
                case 3:{
                    menuController.handleCancelReservation();
                    break;
                }
                case 4:{
                    menuController.handleViewPersonalReservations(user);
                    break;
                }
                case 5: {
                    System.exit(0);
                }
                default: {
                    Loggers.USER_LOGGER.warn("Invalid choice, please try again.(1-5)");
                }
            }
        }
    }
}