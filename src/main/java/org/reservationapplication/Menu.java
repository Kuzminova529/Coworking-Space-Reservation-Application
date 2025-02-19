package org.reservationapplication;

import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);

    public void menu(User user) {
        System.out.println("Welcome to Reservation Application");
        mainMenu(user);
    }

    public void mainMenu(User user) {
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
                        adminMenu();
                    } else {
                        System.out.println("You are not an admin. Choose another option");
                    }
                    break;
                case 2:
                    if (user.getClass()== Customer.class) {
                        customerMenu((Customer) user);
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

    public void adminMenu() {
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
                    addCoworkingSpace();
                    break;
                case 2:
                    removeCoworkingSpace();
                    break;
                case 3:
                    viewAllReservations();
                    break;
                case 4:
                     viewAllCoworkingSpaces();
                     break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void removeCoworkingSpace() {
        System.out.println("Enter id of a coworking space you want to be removed");
        String id = scanner.nextLine();
        GeneralCoworkingSpaceList.removeFromCoworkingSpaceListById(id);
    }
    private void addCoworkingSpace(){
        CoworkingSpace coworkingSpace = new CoworkingSpace();
        System.out.println("Enter type of Coworking space");
        System.out.print("""
                1.Open space
                2.Private
                3.Room
                """);

        int choice = getUserChoiceInt();
        switch (choice){
            case 1:{
                coworkingSpace.setType(TypeOfCoworkingSpace.OPENSPACE);
                break;
            }
            case 2:{
                coworkingSpace.setType(TypeOfCoworkingSpace.PRIVATE);
                break;
            }
            case 3:{
                coworkingSpace.setType(TypeOfCoworkingSpace.ROOM);
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
    private void viewAllReservations(){
        GeneralReservationList.printGeneralCoworkingSpaceList();
    }
    private void viewAllCoworkingSpaces(){
        GeneralCoworkingSpaceList.printGeneralCoworkingSpaceList();
    }


    public void customerMenu(Customer user) {
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
                    browseAvailableSpaces();
                    break;
                }
                case 2: {
                    makeReservation(user);
                    break;
                }
                case 3:{
                    cancelReservation(user);
                    break;
                }
                case 4:{
                    viewPersonalReservations(user);
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

    private void browseAvailableSpaces(){
        List<CoworkingSpace> availableCoworkingSpaceList = GeneralCoworkingSpaceList.getAvailableGeneralCoworkingSpaceList();
        if(availableCoworkingSpaceList.isEmpty()){
            System.out.println("There are no available coworking spaces");
        }
        else {
            System.out.println("There are " + availableCoworkingSpaceList.size() + " available coworking spaces");
            System.out.println(availableCoworkingSpaceList);
        }
    }


    private void makeReservation(Customer user){
        System.out.println("Enter id of coworking space you want to make");
        String id = scanner.nextLine();
        if(GeneralCoworkingSpaceList.isIDMatch(id)){
            Reservation reservation = new Reservation();
            user.addReservationList(reservation);
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

    private void cancelReservation(Customer user) {
        System.out.println("Enter id of a reservation you want to be removed");
        String id = scanner.nextLine();
        if(user.isRemovedFromGeneralReservationListById(id)){
            GeneralReservationList.removeFromGeneralReservationListById(id);
        }
    }

    private void viewPersonalReservations(Customer user) {
        if(!user.getPersonalReservationList().isEmpty())
            System.out.println(user.getPersonalReservationList());
        else
            System.out.println("There are no personal reservation list");
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
