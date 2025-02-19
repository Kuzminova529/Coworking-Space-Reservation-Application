package org.reservationapplication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeneralReservationList {
    private static List<Reservation> generalReservationList= new ArrayList<Reservation>();

    public static void addGeneralReservationList(Reservation reservation) {
        generalReservationList.add(reservation);
    }

    public static void removeFromGeneralReservationListById(String id) {
        Iterator<Reservation> iterator = generalReservationList.iterator();
        while (iterator.hasNext()) {
            Reservation reservation = iterator.next();
            if (reservation.getReservationID().equals(id)) {
                iterator.remove();
                return;
            }
        }
        System.out.println("Reservation with ID " + id + " not found.");
    }


    public static void printGeneralCoworkingSpaceList() {
        for (Reservation reservation : generalReservationList) {
            System.out.println(reservation);
        }
    }


}
