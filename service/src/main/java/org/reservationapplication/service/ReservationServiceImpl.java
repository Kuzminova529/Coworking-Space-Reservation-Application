package org.reservationapplication.service;

import org.reservationapplication.Loggers;
import org.reservationapplication.model.AvailabilityStatus;
import org.reservationapplication.model.Customer;
import org.reservationapplication.model.Reservation;
import org.reservationapplication.model.User;
import org.reservationapplication.repository.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ReservationServiceImpl implements ReservationService {
    Comparator<Reservation> dateTimeComparator = Comparator.comparing(Reservation::getStartDateTime);
    private ReservationRepository reservationRepository;


    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
        reservationRepository.deleteAll();
    }

    public ReservationServiceImpl(TreeSet<Reservation> reservations, ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
        reservationRepository.deleteAll();
        if (reservations != null) {
            reservationRepository.save(reservations);
        }
    }

    public TreeSet<Reservation> getAllReservation() {
        return reservationRepository.read();
    }

    public TreeSet<Reservation> getPersonalReservation(User user) {
        TreeSet<Reservation> personalReservations = reservationRepository.readPersonalReservations(user.getId());
        return personalReservations;
    }

    public TreeSet<Reservation> getReservationsByCoworkingSpaceAndDate(long coworkingSpaceId, LocalDate date) {
        TreeSet<Reservation> reservations = reservationRepository.read();
        return reservations.stream()
                .filter(r -> r.getCoworkingSpaceID() == coworkingSpaceId &&
                        r.getStartDateTime().toLocalDate().equals(date))
                .collect(Collectors.toCollection(() -> new TreeSet<>(dateTimeComparator)));
    }

    public void removeReservationById(long id) {
        reservationRepository.deleteByID(id);
    }

    public void addReservation(Reservation reservation) {
        reservationRepository.add(reservation);
    }

    public boolean userAddReservation(
            long id, String reservationName, LocalDate bookingDate,
            LocalDateTime startDateTime, LocalDateTime endDateTime,
            Customer user, CoworkingSpaceServiceImpl coworkingSpaceService,
            ReservationServiceImpl reservationService) {

            if(coworkingSpaceService.getCoworkingSpaceByID(id).isPresent()) {
                if (coworkingSpaceService.getCoworkingSpaceByID(id).get().getAvailabilityStatus() == AvailabilityStatus.AVAILABLE) {
                    Reservation reservation = new Reservation();
                    reservation.setCoworkingSpaceID(id);
                    reservation.setCustomerID(user.getId());
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

                    TreeSet<Reservation> existingReservations = reservationService.getReservationsByCoworkingSpaceAndDate(id, bookingDate);

                    for (Reservation existing : existingReservations) {

                        LocalDateTime existingStart = existing.getStartDateTime();
                        LocalDateTime existingEnd = existing.getEndDateTime();

                        // The exact same time
                        if (startDateTime.equals(existingStart) && endDateTime.equals(existingEnd)) {
                            Loggers.TECHNICAL_LOGGER.warn("Reservation start time {} and end time {} equals already booked reservation {} and {}", startDateTime, endDateTime, existingStart, existingEnd);
                            Loggers.USER_LOGGER.warn("The reservation time is already booked");

                            throw new IllegalArgumentException("This exact time slot is already booked!");
                        }

                        // Intersection of time
                        if (startDateTime.isBefore(existingEnd) && endDateTime.isAfter(existingStart)) {
                            Loggers.TECHNICAL_LOGGER.warn("Reservation start time {} and end time {} intersect with already booked reservation {} and {}", startDateTime, endDateTime, existingStart, existingEnd);
                            Loggers.USER_LOGGER.warn("The reservation time intersect with is already booked reservation");
                            throw new IllegalArgumentException("This time slot is already booked!");
                        }
                    }

                    reservation.setStartDateTime(startDateTime);
                    reservation.setEndDateTime(endDateTime);
                    reservation.setID(ReservationRepository.getNextId());

                    reservationService.addReservation(reservation);

                    return true;
                }
            }
        return false;
    }
}