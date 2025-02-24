package org.reservationapplication;

import org.reservationapplication.model.*;
import org.reservationapplication.service.CoworkingSpaceServiceImpl;
import org.reservationapplication.service.ReservationServiceImpl;

public class Main {
    public static void main(String[] args) {

        CoworkingSpaceServiceImpl coworkingSpaceService = new CoworkingSpaceServiceImpl();
        ReservationServiceImpl reservationService = new ReservationServiceImpl();

        Menu menu = new Menu();
        User user = new Admin();

        menu.welcomeMenu(user, coworkingSpaceService, reservationService);
    }
}