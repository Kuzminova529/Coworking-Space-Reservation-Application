package org.reservationapplication;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        CoworkingSpace space1 = new CoworkingSpace(TypeOfCoworkingSpace.OPENSPACE,12.1,AvailabilityStatus.AVAILABLE);

        User user1 = new Customer();
        User user2 = new Admin();
        menu.menu(user1);
        menu.menu(user2);

    }
}