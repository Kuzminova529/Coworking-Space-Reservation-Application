package org.reservationapplication;

import org.reservationapplication.model.ApplicationState;
import org.reservationapplication.model.User;
import org.reservationapplication.repository.ApplicationStateRepository;
import org.reservationapplication.repository.CoworkingSpaceRepository;
import org.reservationapplication.service.CacheServiceCoworkingSpace;
import org.reservationapplication.service.CoworkingSpaceServiceImpl;
import org.reservationapplication.service.ReservationServiceImpl;


public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();

        ApplicationStateRepository appStateRepo= new ApplicationStateRepository();
        ApplicationState appState = appStateRepo.readState();

        User user = appState.getCurrentUser();
        CacheServiceCoworkingSpace cacheServiceCoworkingSpace = new CacheServiceCoworkingSpace();
        CoworkingSpaceRepository coworkingSpaceRepo = new CoworkingSpaceRepository();
        CoworkingSpaceServiceImpl coworkingSpaceService = new CoworkingSpaceServiceImpl(appState.getCoworkingSpaces(), cacheServiceCoworkingSpace, coworkingSpaceRepo);
        ReservationServiceImpl reservationService = new ReservationServiceImpl(appState.getReservations());

        menu.welcomeMenu(user, coworkingSpaceService, reservationService);
    }
}