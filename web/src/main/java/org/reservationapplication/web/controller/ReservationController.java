package org.reservationapplication.web.controller;


import jakarta.servlet.http.HttpSession;
import org.reservationapplication.domain.dto.ReservationDto;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.model.User;
import org.reservationapplication.service.CoworkingSpaceService;
import org.reservationapplication.service.ReservationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService service;

    public ReservationController(@Qualifier("reservationServiceImpl") ReservationService service) {
        this.service = service;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    public List<ReservationDto> getAllReservations() {
        return service.getAllReservation();
    }

    @Secured("ROLE_CUSTOMER")
    @GetMapping("/personal")
    public List<ReservationDto> getPersonalReservations(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return new ArrayList<>();
        }
        Long userId = currentUser.getId();
        return service.getPersonalReservation(userId);
    }

    @Secured("ROLE_CUSTOMER")
    @PostMapping("/create")
    public ReservationDto createReservation(@RequestBody ReservationDto reservation) {
        return service.addReservation(reservation);
    }

    @Secured("ROLE_CUSTOMER")
    @DeleteMapping("delete/{id}")
    public boolean deleteReservationById(@PathVariable Long id) {
        return service.removeReservationById(id);
    }
}
