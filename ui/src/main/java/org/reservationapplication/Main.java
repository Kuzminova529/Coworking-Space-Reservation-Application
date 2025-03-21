package org.reservationapplication;

import org.reservationapplication.repository.JPARepos.CoworkingSpaceRepositoryJPA;
import org.reservationapplication.repository.JPARepos.ReservationRepositoryJPA;
import org.reservationapplication.repository.JPARepos.UserRepositoryJPA;
import org.reservationapplication.service.CacheServiceCoworkingSpace;
import org.reservationapplication.service.CoworkingSpaceServiceImpl;
import org.reservationapplication.service.ReservationServiceImpl;
import org.reservationapplication.service.UserService;
import org.reservationapplication.sql.DatabaseMigration;


public class Main {
    public static void main(String[] args) {
        DatabaseMigration.migrate();

        Menu menu = new Menu();

        CacheServiceCoworkingSpace cacheServiceCoworkingSpace = new CacheServiceCoworkingSpace();
        CoworkingSpaceRepositoryJPA coworkingSpaceRepository = new CoworkingSpaceRepositoryJPA();
        CoworkingSpaceServiceImpl coworkingSpaceService = new CoworkingSpaceServiceImpl(cacheServiceCoworkingSpace, coworkingSpaceRepository);

        ReservationRepositoryJPA reservationRepository = new ReservationRepositoryJPA();
        ReservationServiceImpl reservationService = new ReservationServiceImpl(reservationRepository, coworkingSpaceRepository);

        UserRepositoryJPA userRepository = new UserRepositoryJPA();
        UserService userService = new UserService(userRepository);

        menu.welcomeMenu(userService, coworkingSpaceService, reservationService);
    }
}