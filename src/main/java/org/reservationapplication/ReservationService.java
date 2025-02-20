package org.reservationapplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ReservationService {
    private final List<Reservation> personalReservation;
    private final List<Reservation> generalReservationList;


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

    public boolean isAddedReservation(long id, String reservationName, String dateInput, String startTimeInput, String endTimeInput, Customer user, CoworkingSpaceService coworkingSpaceService, ReservationService reservationService){
        if(coworkingSpaceService.isIDMatch(id)){
            Reservation reservation = new Reservation(reservationService);
            reservationService.addPersonalReservation(reservation);
            reservation.setCoworkingSpaceID(id);
            reservation.setCustomerID(user.getId());
            reservation.setReservationName(reservationName);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");


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

            } catch (ParseException e) {
                System.out.println("Invalid date or time format. Try again.");
                return false;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return false;
            }
            return true;
        }
        else {
            System.out.println("Invalid ID");
            return false;
        }

    }


    public void printGeneralReservation() {
        for (Reservation reservation : generalReservationList) {
            System.out.println(reservation);
        }
    }
}
