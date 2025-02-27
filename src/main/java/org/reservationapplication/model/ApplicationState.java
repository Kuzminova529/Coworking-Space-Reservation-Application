package org.reservationapplication.model;

import org.reservationapplication.service.CoworkingSpaceServiceImpl;
import org.reservationapplication.service.ReservationServiceImpl;

import java.util.List;

public class ApplicationState {
    private User currentUser;
    private List<CoworkingSpace> coworkingSpaces;
    private List<Reservation> reservations;

    public ApplicationState() {}

    public ApplicationState(User currentUser, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
        this.currentUser = currentUser;
        this.coworkingSpaces = coworkingSpaceService.getAllCoworkingSpace();
        this.reservations = reservationService.getAllReservation();
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

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
