package org.reservationapplication;

import org.reservationapplication.model.Customer;
import org.reservationapplication.model.User;
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

        Menu menu = new Menu();;

        User user = new Customer();
        CacheServiceCoworkingSpace cacheServiceCoworkingSpace = new CacheServiceCoworkingSpace();
        CoworkingSpaceRepositoryJPA coworkingSpaceRepo = new CoworkingSpaceRepositoryJPA();
        CoworkingSpaceServiceImpl coworkingSpaceService = new CoworkingSpaceServiceImpl(cacheServiceCoworkingSpace, coworkingSpaceRepo);

        ReservationRepositoryJPA reservationRepository = new ReservationRepositoryJPA();
        ReservationServiceImpl reservationService = new ReservationServiceImpl(reservationRepository);

        UserRepositoryJPA userRepository = new UserRepositoryJPA();
        UserService userService = new UserService(userRepository);

        menu.welcomeMenu(userService, coworkingSpaceService, reservationService);
    }
}