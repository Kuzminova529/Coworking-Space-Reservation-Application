package org.reservationapplication.service;

import org.reservationapplication.model.CoworkingSpace;
import org.reservationapplication.model.CoworkingSpaceType;
import org.reservationapplication.model.AvailabilityStatus;
import org.reservationapplication.model.Reservation;
import org.reservationapplication.model.Customer;
import org.reservationapplication.model.User;
import org.reservationapplication.repository.oldRepos.CoworkingSpaceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.TreeSet;

public class MenuService {

    public MenuService(){};

    public void addCoworkingSpace(CoworkingSpaceServiceImpl coworkingSpaceService, int typeChoice, double price, int availabilityChoice) {
        CoworkingSpace coworkingSpace = new CoworkingSpace();
        coworkingSpace.setID(CoworkingSpaceRepository.getNextID());

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

        switch (availabilityChoice) {
            case 1:
                coworkingSpace.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
                break;
            case 2:
                coworkingSpace.setAvailabilityStatus(AvailabilityStatus.UNAVAILABLE);
                break;
            default:
                throw new IllegalArgumentException("Invalid availability choice: " + availabilityChoice);
        }

        coworkingSpaceService.addCoworkingSpace(coworkingSpace);
    }

    public void removeCoworkingSpace(CoworkingSpaceServiceImpl coworkingSpaceService, long id) {
        coworkingSpaceService.removeCoworkingSpace(id);
    }

    public TreeSet<Reservation> viewAllReservations(ReservationServiceImpl reservationService) {
        return reservationService.getAllReservation();
    }

    public List<CoworkingSpace> viewAllCoworkingSpaces(CoworkingSpaceServiceImpl coworkingSpaceService) {
        return coworkingSpaceService.getAllCoworkingSpace();
    }

    public List<CoworkingSpace> viewAvailableSpaces(CoworkingSpaceServiceImpl coworkingSpaceService) {
        return coworkingSpaceService.getAvailableCoworkingSpace();
    }

    public boolean makeReservation(Customer user,
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

    public TreeSet<Reservation> viewPersonalReservations(User user, ReservationServiceImpl reservationService) {
        return reservationService.getPersonalReservation(user);
    }
}