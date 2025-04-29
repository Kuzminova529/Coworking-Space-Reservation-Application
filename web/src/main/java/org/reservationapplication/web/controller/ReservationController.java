package org.reservationapplication.web.controller;


import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.service.ReservationService;
import org.reservationapplication.web.dto.ReservationDto;
import org.reservationapplication.web.mapper.DTOReservationMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService service;
    private final DTOReservationMapper mapper;

    public ReservationController(@Qualifier("reservationServiceImpl") ReservationService service,
                                 @Qualifier("DTOReservationMapper") DTOReservationMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    public List<ReservationDto> getAllReservations() {
        List<ReservationDto> dtos = new ArrayList<>();
        for (Reservation res : service.getAllReservation()) {
            dtos.add(mapper.toDto(res));
        }
        return dtos;
    }

    @Secured("ROLE_CUSTOMER")
    @GetMapping("/personal/{id}")
    public List<ReservationDto> getPersonalReservations(@PathVariable Long id) {
        List<ReservationDto> dtos = new ArrayList<>();
        for (Reservation res : service.getPersonalReservation(id)) {
            dtos.add(mapper.toDto(res));
        }
        return dtos;
    }

    @Secured("ROLE_CUSTOMER")
    @PostMapping("/create")
    public ResponseEntity<?> createReservation(@RequestBody ReservationDto reservation) {
        try {
            Reservation entity = mapper.toEntity(reservation);
            Reservation saved = service.addReservation(entity);

            return ResponseEntity.ok(mapper.toDto(saved));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating reservation: " + e.getMessage());
        }
    }

    @Secured("ROLE_CUSTOMER")
    @DeleteMapping("delete/{id}")
    public boolean deleteReservationById(@PathVariable Long id) {
        return service.removeReservation(id);
    }
}
