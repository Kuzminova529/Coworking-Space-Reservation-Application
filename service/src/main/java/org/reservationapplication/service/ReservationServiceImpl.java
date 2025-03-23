package org.reservationapplication.service;

import org.reservationapplication.Loggers;
import org.reservationapplication.exeption.CoworkingSpaceNotFoundException;
import org.reservationapplication.model.CoworkingSpace;
import org.reservationapplication.model.Reservation;
import org.reservationapplication.model.User;
import org.reservationapplication.repository.JDBCRepos.CoworkingSpaceRepository;
import org.reservationapplication.repository.JPARepos.CoworkingSpaceRepositoryJPA;
import org.reservationapplication.repository.JPARepos.ReservationRepositoryJPA;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ReservationServiceImpl implements ReservationService {
    private ReservationRepositoryJPA reservationRepository;
    private CoworkingSpaceRepositoryJPA coworkingSpaceRepository;


    public ReservationServiceImpl(ReservationRepositoryJPA reservationRepository, CoworkingSpaceRepositoryJPA coworkingSpaceRepository) {
        this.reservationRepository = reservationRepository;
        this.coworkingSpaceRepository = coworkingSpaceRepository;
    }

    public TreeSet<Reservation> getAllReservation() {
        return reservationRepository.read();
    }

    public TreeSet<Reservation> getPersonalReservation(User user) {
        return reservationRepository.readPersonalReservations(user.getId());
    }

    public void removeReservationById(long id) {
        reservationRepository.updateReservationStatus(id);
    }

    public void addReservation(Reservation reservation) {
        reservationRepository.create(reservation);
    }

    public boolean isTimeSlotAvailable(Long coworkingSpaceId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        CoworkingSpace coworkingSpace = coworkingSpaceRepository.getCoworkingSpaceWithReservations(coworkingSpaceId);
        if (coworkingSpace == null) {
            throw new CoworkingSpaceNotFoundException(coworkingSpaceId,404);
        }

        for (Reservation reservation : coworkingSpace.getReservations()) {
            if ((startDateTime.isBefore(reservation.getEndDateTime()) && endDateTime.isAfter(reservation.getStartDateTime())) ||
                    (startDateTime.equals(reservation.getStartDateTime()) || endDateTime.equals(reservation.getEndDateTime()))) {
                return false;
            }
        }
        return true;
    }

    public boolean userAddReservation(
            long coworkingID, String reservationName, LocalDate bookingDate,
            LocalDateTime startDateTime, LocalDateTime endDateTime,
            User user, CoworkingSpaceServiceImpl coworkingSpaceService,
            ReservationServiceImpl reservationService) {

        Optional<CoworkingSpace> optionalCoworkingSpace = coworkingSpaceService.getCoworkingSpaceByID(coworkingID);

        if (optionalCoworkingSpace.isEmpty()) {
            throw new IllegalArgumentException("Invalid id of coworkingSpace");
        }

        CoworkingSpace coworkingSpace = optionalCoworkingSpace.get();

        if (!coworkingSpace.getActive()) {
            throw new IllegalArgumentException("This coworkingSpace is not active");
        }


        Reservation reservation = new Reservation();
        reservation.setCoworkingSpace(coworkingSpace);
        reservation.setUserID(user.getId());
        reservation.setReservationName(reservationName);

        LocalDate today = LocalDate.now();

        if (bookingDate.isBefore(today)) {
            Loggers.TECHNICAL_LOGGER.warn("Attempted to register a booking with a past date: {}", bookingDate);

            throw new IllegalArgumentException("You cannot register a past date!");
        }
        if (!startDateTime.isBefore(endDateTime)) {
            Loggers.TECHNICAL_LOGGER.warn("Reservation start time {} is not before end time {}", startDateTime, endDateTime);
            Loggers.USER_LOGGER.warn("The reservation start time must be before the end time!");

            throw new IllegalArgumentException("The reservation start time must be before the end time!");
        }

        if (!isTimeSlotAvailable(coworkingID ,startDateTime, endDateTime)) {
            throw new IllegalArgumentException("This reservation time is booked");
        }


        reservation.setStartDateTime(startDateTime);
        reservation.setEndDateTime(endDateTime);

        reservation.setActive(true);

        reservationService.addReservation(reservation);
        return true;
    }
}