package org.reservationapplication;

import java.util.Iterator;
import java.util.List;

public class ReservationService {
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


    public void printGeneralReservation() {
        for (Reservation reservation : generalReservationList) {
            System.out.println(reservation);
        }
    }
}
