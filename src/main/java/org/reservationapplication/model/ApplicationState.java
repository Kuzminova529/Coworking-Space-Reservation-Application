package org.reservationapplication.model;

import org.reservationapplication.service.CoworkingSpaceService;
import org.reservationapplication.service.ReservationService;

public class ApplicationState {
    private User currentUser;
    private CoworkingSpaceService coworkingSpaceService;
    private ReservationService reservationService;

    public ApplicationState() {}

    public ApplicationState(User currentUser, CoworkingSpaceService coworkingSpaceService, ReservationService reservationService) {
        this.currentUser = currentUser;
        this.coworkingSpaceService = coworkingSpaceService;
        this.reservationService = reservationService;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public CoworkingSpaceService getCoworkingSpaceService() {
        return coworkingSpaceService;
    }

    public void setCoworkingSpaceService(CoworkingSpaceService coworkingSpaceService) {
        this.coworkingSpaceService = coworkingSpaceService;
    }

    public ReservationService getReservationService() {
        return reservationService;
    }

    public void setReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
}
