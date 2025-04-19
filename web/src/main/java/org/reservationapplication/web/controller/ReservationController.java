package org.reservationapplication.web.controller;


import jakarta.servlet.http.HttpSession;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.model.User;
import org.reservationapplication.service.ReservationService;
import org.reservationapplication.web.dto.CoworkingSpaceDto;
import org.reservationapplication.web.dto.ReservationDto;
import org.reservationapplication.web.mapper.DTOReservationMapper;
import org.springframework.beans.factory.annotation.Qualifier;
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
        for(Reservation res : service.getAllReservation()){
            dtos.add(mapper.toDto(res));
        }
        return dtos;
    }

    @Secured("ROLE_CUSTOMER")
    @GetMapping("/personal")
    public List<ReservationDto> getPersonalReservations(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return new ArrayList<>();
        }
        Long userId = currentUser.getId();
        List<ReservationDto> dtos = new ArrayList<>();
        for(Reservation res : service.getPersonalReservation(userId)){
            dtos.add(mapper.toDto(res));
        }
        return dtos;
    }

    @Secured("ROLE_CUSTOMER")
    @PostMapping("/create")
    public ReservationDto createReservation(@RequestBody ReservationDto reservation) {
        return mapper.toDto(service.addReservation(mapper.toEntity(reservation)));
    }

    @Secured("ROLE_CUSTOMER")
    @DeleteMapping("delete/{id}")
    public boolean deleteReservationById(@PathVariable Long id) {
        return service.removeReservationById(id);
    }
}
