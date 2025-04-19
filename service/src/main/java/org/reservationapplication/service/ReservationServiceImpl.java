package org.reservationapplication.service;

import org.reservationapplication.domain.builder.ReservationBuilder;
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

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(@Qualifier("reservationRepositorySpring") ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Reservation> getAllReservation() {
        try {
            return reservationRepository.findAll();
        } catch (DatabaseException e) {
            throw new BusinessException("Failed to retrieve reservations", e);
        }
    }

    @Override
    public List<Reservation> getPersonalReservation(Long id) {
        try {
            return reservationRepository.readPersonalReservations(id);
        } catch (DatabaseException e) {
            throw new BusinessException("Failed to retrieve reservations", e);
        }
    }

    @Override
    public Reservation findReservationById(Long id) {
        try {
            Optional<Reservation> optReservation = reservationRepository.getByIdOptional(id);
            if (optReservation.isPresent()) {
                return optReservation.get();
            } else {
                throw new BusinessException("Failed to find reservation");
            }
        } catch (DatabaseException e) {
            throw new BusinessException("Failed to find coworking space by id");
        }
    }

    @Override
    public boolean removeReservationById(long id) {
        try {
            findReservationById(id);
        } catch (BusinessException e) {
            throw new BusinessException("Failed to remove reservation", e);
        }
        try {
            reservationRepository.updateStatus(id);
            return true;
        } catch (DatabaseException e) {
            throw new BusinessException("Failed to update reservation", e);
        }
    }

    @Override
    public Reservation addReservation(Reservation reservation) {
        try {
            reservationRepository.save(reservation);
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

            Reservation reservation = new ReservationBuilder()
                    .setCoworkingSpace(coworkingSpace)
                    .setUserId(user.getId())
                    .setReservationName(reservationName)
                    .setStartDateTime(startDateTime)
                    .setEndDateTime(endDateTime)
                    .setActive(true)
                    .build();
            addReservation(reservation);
            return reservation;
        } catch (DatabaseException e) {
            throw new BusinessException("Failed to add reservation", e);
        }
    }
}