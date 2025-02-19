package org.reservationapplication;

import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);

    public void welcomeMenu(User user, CoworkingSpaceService coworkingSpaceService, ReservationService reservationService) {
        System.out.println("Welcome to Reservation Application");
        mainMenu(user, coworkingSpaceService, reservationService);
    }

    public void mainMenu(User user, CoworkingSpaceService coworkingSpaceService, ReservationService reservationService) {
        while (true) {
            System.out.println("""
                    1. Admin menu
                    2. Customer menu
                    3. Exit
                    """);
            int choice = getUserChoiceInt();
            switch (choice) {
                case 1:
                    if ((user.getClass()== Admin.class)) {
                        adminMenu(coworkingSpaceService, reservationService);
                    } else {
                        System.out.println("You are not an admin. Choose another option");
                    }
                    break;
                case 2:
                    if (user.getClass()== Customer.class) {
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

    public void adminMenu(CoworkingSpaceService coworkingSpaceService, ReservationService reservationService) {
        while (true) {
            System.out.println("""
                    1. Add a new coworking space
                    2. Remove a coworking space
                    3. View all reservations
                    4. View all coworking spaces
                    5. Back to Main Menu
                    """);
            int choice = getUserChoiceInt();
            switch (choice) {
                case 1:
                    addCoworkingSpace(coworkingSpaceService);
                    break;
                case 2:
                    removeCoworkingSpace(coworkingSpaceService);
                    break;
                case 3:
                    viewAllReservations(reservationService);
                    break;
                case 4:
                     viewAllCoworkingSpaces(coworkingSpaceService);
                     break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void removeCoworkingSpace(CoworkingSpaceService generalCoworkingSpace) {
        System.out.println("Enter id of a coworking space you want to be removed");
        long id = getUserChoiceLong();
        generalCoworkingSpace.removeFromCoworkingSpaceById(id);
    }
    private void addCoworkingSpace(CoworkingSpaceService coworkingSpaceService) {
        CoworkingSpace coworkingSpace = new CoworkingSpace(coworkingSpaceService);
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
            }
        }

    }
    private void viewAllReservations(ReservationService reservationService) {
        reservationService.printGeneralReservation();
    }
    private void viewAllCoworkingSpaces(CoworkingSpaceService coworkingSpaceService){
        coworkingSpaceService.printGeneralCoworkingSpace();
    }


    public void customerMenu(Customer user, CoworkingSpaceService coworkingSpaceService, ReservationService reservationService) {
        while (true) {
            System.out.println("""
                    1. Browse available spaces
                    2. Make a reservation
                    3. Cancel reservation
                    4. View my reservations
                    5. Back to Main Menu
                    """);
            int choice = getUserChoiceInt();
            switch (choice) {
                case 1: {
                    browseAvailableSpaces(coworkingSpaceService);
                    break;
                }
                case 2: {
                    makeReservation(user, coworkingSpaceService, reservationService);
                    break;
                }
                case 3:{
                    cancelReservation(reservationService);
                    break;
                }
                case 4:{
                    viewPersonalReservations(reservationService);
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

    private void browseAvailableSpaces(CoworkingSpaceService coworkingSpaceService) {
        List<CoworkingSpace> availableCoworkingSpaceList = coworkingSpaceService.getAvailableGeneralCoworkingSpace();
        if(availableCoworkingSpaceList.isEmpty()){
            System.out.println("There are no available coworking spaces");
        }
        else {
            System.out.println("There are " + availableCoworkingSpaceList.size() + " available coworking spaces");
            System.out.println(availableCoworkingSpaceList);
        }
    }


    private void makeReservation(Customer user, CoworkingSpaceService coworkingSpaceService, ReservationService reservationService) {
        System.out.println("Enter id of coworking space you want to make");
        long id = getUserChoiceLong();
        if(coworkingSpaceService.isIDMatch(id)){
            Reservation reservation = new Reservation(reservationService);
            reservationService.addPersonalReservation(reservation);
            reservation.setCoworkingSpaceID(id);
            reservation.setCustomerID(user.getId());
            System.out.println("Enter name for reservation");
            reservation.setReservationName(scanner.nextLine());
            while (true){
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                System.out.println("Enter the reservation date (for example, 31.12.2025):");
                String dateInput = scanner.nextLine();

                System.out.println("Enter the start time of the reservation (for example, 10:00):");
                String startTimeInput = scanner.nextLine();

                System.out.println("Enter the end time of the reservation (for example, 12:00):");
                String endTimeInput = scanner.nextLine();

                try {
                    Calendar today = Calendar.getInstance();
                    today.set(Calendar.HOUR_OF_DAY, 0);
                    today.set(Calendar.MINUTE, 0);
                    today.set(Calendar.SECOND, 0);
                    today.set(Calendar.MILLISECOND, 0);

                    Calendar bookingDate = Calendar.getInstance();
                    bookingDate.setTime(dateFormat.parse(dateInput));


                    if (bookingDate.before(today)) {
                        throw new IllegalArgumentException("You cannot book for a past date!");
                    }

                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTime(dateFormat.parse(dateInput));
                    Date startTime = timeFormat.parse(startTimeInput);
                    startCalendar.set(Calendar.HOUR_OF_DAY, startTime.getHours());
                    startCalendar.set(Calendar.MINUTE, startTime.getMinutes());


                    Calendar endCalendar = Calendar.getInstance();
                    endCalendar.setTime(dateFormat.parse(dateInput));
                    Date endTime = timeFormat.parse(endTimeInput);
                    endCalendar.set(Calendar.HOUR_OF_DAY, endTime.getHours());
                    endCalendar.set(Calendar.MINUTE, endTime.getMinutes());


                    if (startCalendar.after(endCalendar) || startCalendar.equals(endCalendar)) {
                        throw new IllegalArgumentException("The start time of the reservation must be before the end time!");
                    }

                    reservation.setStartReservationDateAndTime(startCalendar);
                    reservation.setEndReservationDateAndTime(endCalendar);
                    break;

                } catch (ParseException e) {
                    System.out.println("Invalid date or time format. Try again.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        else System.out.println("Invalid ID");


    }

    private void cancelReservation( ReservationService reservationService) {
        System.out.println("Enter id of a reservation you want to be removed");
        long id = getUserChoiceLong();
        if(reservationService.removePersonalReservationById(id)){
            reservationService.removeFromGeneralReservationById(id);
        }
    }

    private void viewPersonalReservations(ReservationService reservationService) {
        if(!reservationService.getPersonalReservation().isEmpty())
            System.out.println(reservationService.getPersonalReservation());
        else
            System.out.println("There are no personal reservation list");
    }

    private long getUserChoiceLong() {
        while (true) {
            try {
                System.out.print("Enter: ");
                long choice = scanner.nextLong();
                scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }


    private int getUserChoiceInt() {
        while (true) {
            try {
                System.out.print("Enter: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }

    private double getUserChoiceDouble() {
        while (true) {
            try {
                System.out.print("Enter: ");
                double choice = scanner.nextDouble();
                scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a double number.");
                scanner.next();
            }
        }
    }

}
