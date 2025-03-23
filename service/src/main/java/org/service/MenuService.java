package org.service;

import org.domain.model.CoworkingSpace;
import org.domain.model.CoworkingSpaceType;
import org.domain.model.Reservation;
import org.domain.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class MenuService {

    public MenuService(){};

    public void addCoworkingSpace(CoworkingSpaceServiceImpl coworkingSpaceService, int typeChoice, double price) {
        CoworkingSpace coworkingSpace = new CoworkingSpace();

        switch (typeChoice) {
            case 1:
                coworkingSpace.setType(CoworkingSpaceType.OPENSPACE);
                break;
            case 2:
                coworkingSpace.setType(CoworkingSpaceType.PRIVATE);
                break;
            case 3:
                coworkingSpace.setType(CoworkingSpaceType.ROOM);
                break;
            default:
                throw new IllegalArgumentException("Invalid coworking space type choice: " + typeChoice);
        }

        coworkingSpace.setPrice(price);

        coworkingSpace.setActive(true);

        coworkingSpaceService.addCoworkingSpace(coworkingSpace);
    }

    public void removeCoworkingSpace(CoworkingSpaceServiceImpl coworkingSpaceService, long id) {
        coworkingSpaceService.removeCoworkingSpace(id);
    }

    public List<Reservation> viewAllReservations(ReservationServiceImpl reservationService) {
        return reservationService.getAllReservation();
    }

    public List<CoworkingSpace> viewAllCoworkingSpaces(CoworkingSpaceServiceImpl coworkingSpaceService) {
        return coworkingSpaceService.getAllCoworkingSpace();
    }

    public List<CoworkingSpace> viewAvailableSpaces(CoworkingSpaceServiceImpl coworkingSpaceService) {
        return coworkingSpaceService.getAvailableCoworkingSpace();
    }

    public boolean makeReservation(User user,
                                   CoworkingSpaceServiceImpl coworkingSpaceService,
                                   ReservationServiceImpl reservationService,
                                   long coworkingSpaceID,
                                   String reservationName,
                                   LocalDate bookingDate,
                                   LocalTime startTime,
                                   LocalTime endTime) {
        LocalDateTime startDateTime = LocalDateTime.of(bookingDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(bookingDate, endTime);

        return reservationService.userAddReservation(coworkingSpaceID, reservationName, bookingDate, startDateTime, endDateTime, user, coworkingSpaceService, reservationService);
    }

    public void cancelReservation(ReservationServiceImpl reservationService, long reservationId) {
        reservationService.removeReservationById(reservationId);
    }

    public List<Reservation> viewPersonalReservations(User user, ReservationServiceImpl reservationService) {
        return reservationService.getPersonalReservation(user);
    }
}