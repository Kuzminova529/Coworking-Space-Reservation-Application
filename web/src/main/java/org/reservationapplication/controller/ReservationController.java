package org.reservationapplication.controller;


import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.model.User;
import org.reservationapplication.service.CoworkingSpaceService;
import org.reservationapplication.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return service.getAllReservation();
    }

    @GetMapping
    public List<Reservation> getPersonalReservations(@RequestParam User user) {
        return service.getPersonalReservation(user);
    }

    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation) {
        return service.addReservation(reservation);
    }

    @PostMapping
    public Reservation userCreateReservation(
            @RequestBody long coworkingID, @RequestBody String reservationName, @RequestBody LocalDate bookingDate,
            @RequestBody LocalDateTime startDateTime, @RequestBody LocalDateTime endDateTime,
            @RequestBody User user, @RequestBody CoworkingSpaceService coworkingSpaceService) {
        return service.userAddReservation(coworkingID, reservationName, bookingDate, startDateTime, endDateTime, user, coworkingSpaceService);
    }

    @DeleteMapping
    public boolean deleteReservationById(@RequestBody Long reservationId) {
        return service.removeReservationById(reservationId);
    }

}
