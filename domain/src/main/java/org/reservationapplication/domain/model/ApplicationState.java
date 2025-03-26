package org.reservationapplication.domain.model;

import java.util.List;
import java.util.TreeSet;

public class ApplicationState {
    private User currentUser;
    private List<CoworkingSpace> coworkingSpaces;
    private TreeSet<Reservation> reservations;

    public ApplicationState() {}

    public ApplicationState(User currentUser, List<CoworkingSpace> coworkingSpaces, TreeSet<Reservation> reservations) {
        this.currentUser = currentUser;
        this.coworkingSpaces = coworkingSpaces;
        this.reservations = reservations;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<CoworkingSpace> getCoworkingSpaces() {
        return coworkingSpaces;
    }

    public void setCoworkingSpaces(List<CoworkingSpace> coworkingSpaces) {
        this.coworkingSpaces = coworkingSpaces;
    }

    public TreeSet<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(TreeSet<Reservation> reservations) {
        this.reservations = reservations;
    }
}
