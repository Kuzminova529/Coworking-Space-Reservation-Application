package org.reservationapplication;

import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        List<CoworkingSpace> generalCoworkingSpace = new ArrayList<>();
        CoworkingSpaceService coworkingSpaceService = new CoworkingSpaceService(generalCoworkingSpace);

        List<Reservation> personalReservation = new ArrayList<>();
        List<Reservation> generalReservationList = new ArrayList<>();
        ReservationService reservationService = new ReservationService(personalReservation, generalReservationList);

        Menu menu = new Menu();
        CoworkingSpace space1 = new CoworkingSpace(coworkingSpaceService, CoworkingSpaceType.OPENSPACE,12.1,AvailabilityStatus.AVAILABLE);

        User user1 = new Customer();//creating user

        menu.welcomeMenu(user1, coworkingSpaceService, reservationService);//load menu


    }
}