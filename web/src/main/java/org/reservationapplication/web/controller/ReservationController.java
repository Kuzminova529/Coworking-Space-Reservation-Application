package org.reservationapplication.web.controller;


import org.reservationapplication.domain.dto.ReservationDto;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.model.User;
import org.reservationapplication.service.CoworkingSpaceService;
import org.reservationapplication.service.ReservationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService service;

    public ReservationController(@Qualifier("reservationServiceImpl") ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReservationDto> getAllReservations() {
        return service.getAllReservation();
    }

    @GetMapping("/{id}")
    public List<ReservationDto> getPersonalReservations(@PathVariable Long id) {
        return service.getPersonalReservation(id);
    }

    @PostMapping
    public ReservationDto createReservation(@RequestBody ReservationDto reservation) {
        return service.addReservation(reservation);
    }

    @PostMapping("/user")
    public ReservationDto userCreateReservation(
            @RequestBody long coworkingID, @RequestBody String reservationName, @RequestBody LocalDate bookingDate,
            @RequestBody LocalDateTime startDateTime, @RequestBody LocalDateTime endDateTime,
            @RequestBody User user, @RequestBody CoworkingSpaceService coworkingSpaceService) {
        return service.userAddReservation(coworkingID, reservationName, bookingDate, startDateTime, endDateTime, user, coworkingSpaceService);
    }

    @DeleteMapping("/{id}")
    public boolean deleteReservationById(@PathVariable Long id) {
        return service.removeReservationById(id);
    }

}
