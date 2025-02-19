package org.reservationapplication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Customer extends User {
    private final boolean adminRoleStatus = false;
    private List<Reservation> personalReservationList = new ArrayList<Reservation>();


    public List<Reservation> getPersonalReservationList() {
        return personalReservationList;
    }

    public void addReservationList(Reservation reservation) {
        personalReservationList.add(reservation);
    }

    public boolean isRemovedFromGeneralReservationListById(String id) {
        Iterator<Reservation> iterator = personalReservationList.iterator();
        while (iterator.hasNext()) {
            Reservation reservation = iterator.next();
            if (reservation.getReservationID().equals(id)) {
                iterator.remove();
                System.out.println("Reservation with ID " + id + " has been removed.");
                return true;
            }
        }
        System.out.println("Reservation with ID " + id + " not found.");
        return false;
    }



}
