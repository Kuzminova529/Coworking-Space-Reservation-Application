package org.reservationapplication;

import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ReservationService {
    Scanner scanner = new Scanner(System.in);
    private final List<Reservation> personalReservation;
    private List<Reservation> generalReservationList;


    public ReservationService(List<Reservation> personalReservation, List<Reservation> generalReservationList) {
        this.personalReservation = personalReservation;
        this.generalReservationList = generalReservationList;
    }

    public List<Reservation> getPersonalReservation() {
        return personalReservation;
    }

    public boolean removePersonalReservationById(long id) {
        Iterator<Reservation> iterator = personalReservation.iterator();
        while (iterator.hasNext()) {
            Reservation reservation = iterator.next();
            if (reservation.getReservationID() == id) {
                iterator.remove();
                System.out.println("Reservation with ID " + id + " has been removed.");
                return true;
            }
        }
        System.out.println("Reservation with ID " + id + " not found.");
        return false;
    }

    public void addPersonalReservation(Reservation reservation) {
        personalReservation.add(reservation);
    }

    public void addGeneralReservation(Reservation reservation) {
        generalReservationList.add(reservation);
    }

    public void removeFromGeneralReservationById(long id) {
        Iterator<Reservation> iterator = generalReservationList.iterator();
        while (iterator.hasNext()) {
            Reservation reservation = iterator.next();
            if (reservation.getReservationID()==id) {
                iterator.remove();
                return;
            }
        }
        System.out.println("Reservation with ID " + id + " not found.");
    }

    public void addReservation(long id, Customer user, CoworkingSpaceService coworkingSpaceService, ReservationService reservationService){
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


    public void printGeneralReservation() {
        for (Reservation reservation : generalReservationList) {
            System.out.println(reservation);
        }
    }
}
