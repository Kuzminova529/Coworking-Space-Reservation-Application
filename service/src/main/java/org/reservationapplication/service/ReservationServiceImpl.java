package org.reservationapplication.service;

import org.reservationapplication.domain.builder.ReservationBuilder;
import org.reservationapplication.domain.exeption.BusinessException;
import org.reservationapplication.domain.exeption.CoworkingSpaceNotFoundException;
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
            Optional<Reservation> optReservation = reservationRepository.findByIdCustom(id);
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
    public boolean removeReservation(long id) {
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

            LocalDateTime today = LocalDateTime.now();

            if (reservation.getStartDateTime().isBefore(today)) {
                Loggers.TECHNICAL_LOGGER.warn("Attempted to register a booking with a past date: {}", reservation.getStartDateTime());

                throw new IllegalArgumentException("You cannot register a past date!");
            }
            if (!reservation.getStartDateTime().isBefore(reservation.getEndDateTime())) {
                Loggers.TECHNICAL_LOGGER.warn("Reservation start time {} is not before end time {}", reservation.getStartDateTime(), reservation.getEndDateTime());

                throw new IllegalArgumentException("The reservation start time must be before the end time!");
            }

            if (!isTimeSlotAvailable(reservation.getCoworkingSpace().getId(), reservation.getStartDateTime(), reservation.getEndDateTime())) {
                throw new IllegalArgumentException("This reservation time is booked");
            }

            reservationRepository.save(reservation);
            return reservation;
        } catch (DatabaseException e) {
            throw new BusinessException("Failed to add reservation", e);
        }
    }

    @Override
    public boolean isTimeSlotAvailable(Long coworkingSpaceId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        try {
            for (Reservation reservation : reservationRepository.findAll()) {
                if (Objects.equals(coworkingSpaceId, reservation.getCoworkingSpace().getId()) && ((startDateTime.isBefore(reservation.getEndDateTime()) && endDateTime.isAfter(reservation.getStartDateTime())) ||
                        (startDateTime.equals(reservation.getStartDateTime()) || endDateTime.equals(reservation.getEndDateTime())))) {
                    return false;
                }
            }
            return true;
        } catch (DatabaseException e){
            throw new BusinessException("Failed to check if time slots are available", e);
        }
    }
}