package org.reservationapplication.service;

import org.reservationapplication.domain.exeption.BusinessException;
import org.reservationapplication.domain.exeption.DatabaseException;
import org.reservationapplication.domain.repository.ReservationRepository;
import org.reservationapplication.logger.Loggers;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;

@Service()
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(@Qualifier("jpaReservationRepository") ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservation() {
        try {
            return reservationRepository.read();
        } catch (DatabaseException e){
            throw new BusinessException("Failed to retrieve reservations", e);
        }
    }

    public List<Reservation> getPersonalReservation(Long id) {
        try {
            List<Reservation> reservations = reservationRepository.readPersonalReservations(id);
            return reservations;
        } catch (DatabaseException e){
            throw new BusinessException("Failed to retrieve reservations", e);
        }
    }

    public boolean removeReservationById(long id) {
        try {
            reservationRepository.updateStatus(id);
            return true;
        } catch (DatabaseException e) {
            throw new BusinessException("Failed to update reservation", e);
        }
    }

    public Reservation addReservation(Reservation reservation) {
        try {
            reservationRepository.create(reservation);
            return reservation;
        } catch (DatabaseException e) {
            throw new BusinessException("Failed to add reservation", e);
        }
    }

    @Override
    public Reservation userAddReservation(
            long coworkingID, String reservationName, LocalDate bookingDate,
            LocalDateTime startDateTime, LocalDateTime endDateTime,
            User user, CoworkingSpaceService coworkingSpaceService) {

        try {

            CoworkingSpace coworkingSpace = coworkingSpaceService.getCoworkingSpaceByID(coworkingID);

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

            if (!coworkingSpaceService.isTimeSlotAvailable(coworkingID, startDateTime, endDateTime)) {
                throw new IllegalArgumentException("This reservation time is booked");
            }


            reservation.setStartDateTime(startDateTime);
            reservation.setEndDateTime(endDateTime);

            reservation.setActive(true);

            addReservation(reservation);
            return reservation;
        } catch (DatabaseException e) {
            throw new BusinessException("Failed to add reservation", e);
        }
    }
}