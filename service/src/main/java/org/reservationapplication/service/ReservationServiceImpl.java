package org.reservationapplication.service;

import org.reservationapplication.domain.builder.ReservationBuilder;
import org.reservationapplication.domain.dto.CoworkingSpaceDto;
import org.reservationapplication.domain.dto.ReservationDto;
import org.reservationapplication.domain.exeption.BusinessException;
import org.reservationapplication.domain.exeption.DatabaseException;
import org.reservationapplication.domain.repository.SpringDataJPARepos.ReservationRepositorySpring;
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

    private ReservationRepositorySpring reservationRepository;
    private CoworkingSpaceService coworkingSpaceService;

    @Autowired
    public ReservationServiceImpl(@Qualifier("reservationRepositorySpring") ReservationRepositorySpring reservationRepository,
                                  @Qualifier("coworkingSpaceServiceImpl") CoworkingSpaceService coworkingSpaceService) {
        this.reservationRepository = reservationRepository;
        this.coworkingSpaceService = coworkingSpaceService;
    }

    public ReservationDto toDto(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                reservation.getCoworkingSpace().getId(),
                reservation.getUserID(),
                reservation.getReservationName(),
                reservation.getStartDateTime(),
                reservation.getEndDateTime(),
                reservation.getActive()
        );
    }

    public Reservation toEntity(ReservationDto dto) {
        CoworkingSpace coworkingSpace = coworkingSpaceService.getCoworkingSpaceByIDForReservation(dto.getCoworkingSpaceId());
        Reservation reservation = new ReservationBuilder()
                .setCoworkingSpace(coworkingSpace)
                .setReservationName(dto.getReservationName())
                .setStartDateTime(dto.getStartDateTime())
                .setEndDateTime(dto.getEndDateTime())
                .setUserId(dto.getUserID())
                .setActive(true)
                .build();

        return reservation;
    }

    @Override
    public List<ReservationDto> getAllReservation() {
        try {
            return reservationRepository.findAll().stream()
                    .map(this::toDto)
                    .toList();
        } catch (DatabaseException e) {
            throw new BusinessException("Failed to retrieve reservations", e);
        }
    }

    @Override
    public List<ReservationDto> getPersonalReservation(Long id) {
        try {
            return reservationRepository.readPersonalReservations(id).stream()
                    .map(this::toDto)
                    .toList();
        } catch (DatabaseException e) {
            throw new BusinessException("Failed to retrieve reservations", e);
        }
    }

    @Override
    public ReservationDto findReservationById(Long id) {
        try {
            Optional<Reservation> optReservation = reservationRepository.findById(id);
            if (optReservation.isPresent()) {
                return toDto(optReservation.get());
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
    public ReservationDto addReservation(ReservationDto dto) {
        try {
            Reservation reservation = toEntity(dto);
            reservationRepository.save(reservation);
            return dto;
        } catch (DatabaseException e) {
            throw new BusinessException("Failed to add reservation", e);
        }
    }

    @Override
    public ReservationDto userAddReservation(
            long coworkingID, String reservationName, LocalDate bookingDate,
            LocalDateTime startDateTime, LocalDateTime endDateTime,
            User user, CoworkingSpaceService coworkingSpaceService) {

        try {

            CoworkingSpaceDto dto = coworkingSpaceService.getCoworkingSpaceByID(coworkingID);
            CoworkingSpace coworkingSpace = coworkingSpaceService.toEntity(dto);

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
            addReservation(toDto(reservation));
            return toDto(reservation);
        } catch (DatabaseException e) {
            throw new BusinessException("Failed to add reservation", e);
        }
    }
}