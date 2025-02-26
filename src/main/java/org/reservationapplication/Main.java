package org.reservationapplication;

import org.reservationapplication.model.*;
import org.reservationapplication.repository.ApplicationStateRepository;
import org.reservationapplication.service.CoworkingSpaceServiceImpl;
import org.reservationapplication.service.ReservationServiceImpl;

public class Main {
    public static void main(String[] args) {

        Menu menu = new Menu();

        ApplicationStateRepository appStateRepo= new ApplicationStateRepository();
        ApplicationState appState = appStateRepo.readState();

        User user = appState.getCurrentUser();
        CoworkingSpaceServiceImpl coworkingSpaceService = new CoworkingSpaceServiceImpl(appState.getCoworkingSpaces());
        ReservationServiceImpl reservationService = new ReservationServiceImpl(appState.getReservations());

        menu.welcomeMenu(user, coworkingSpaceService, reservationService);
    }
}