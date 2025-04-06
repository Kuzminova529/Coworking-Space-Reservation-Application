package org.reservationapplication.web.controller;

import jakarta.servlet.http.HttpSession;
import org.reservationapplication.domain.dto.CoworkingSpaceDto;
import org.reservationapplication.domain.dto.ReservationDto;
import org.reservationapplication.domain.model.User;
import org.reservationapplication.service.CoworkingSpaceService;
import org.reservationapplication.service.ReservationService;
import org.reservationapplication.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    CoworkingSpaceService coworkingSpaceService;
    ReservationService reservationService;
    UserService userService;

    public CustomerController(@Qualifier("coworkingSpaceServiceImpl") CoworkingSpaceService coworkingSpaceService,
                              @Qualifier("reservationServiceImpl") ReservationService reservationService,
                              @Qualifier("userService") UserService userService
    ) {
        this.coworkingSpaceService = coworkingSpaceService;
        this.reservationService = reservationService;
        this.userService = userService;
    }

    @GetMapping("/browse-spaces")
    @ResponseBody
    public List<CoworkingSpaceDto> browseSpaces() {
        return coworkingSpaceService.getAllCoworkingSpace();
    }

    @GetMapping("my-reservations")
    @ResponseBody
    public List<ReservationDto> myReservations(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return new ArrayList<>();
        }

        Long userId = currentUser.getId();
        return reservationService.getPersonalReservation(userId);
    }


    @GetMapping("/make-reservation")
    public String showMakeReservationForm(Model model) {
        List<CoworkingSpaceDto> coworkingSpaces = coworkingSpaceService.getAllCoworkingSpace();

        model.addAttribute("coworkingSpaces", coworkingSpaces);
        model.addAttribute("reservation", new ReservationDto());
        return "make-reservation";
    }

    @PostMapping("/make-reservation")
    public String makeReservation(@ModelAttribute("reservation") ReservationDto reservationDto, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        reservationDto.setUserID(currentUser.getId());
        reservationDto.setActive(true);
        reservationService.addReservation(reservationDto);
        return "redirect:/success";
    }

    @GetMapping("/cancel-reservation")
    public String showCancelReservationForm(Model model) {
        model.addAttribute("reservation", new ReservationDto());
        return "cancel-reservation";
    }

    @PostMapping("/cancel-reservation")
    public String cancelReservation(@ModelAttribute("reservation") ReservationDto reservationDto) {
        reservationService.removeReservationById(reservationDto.getId());
        return "redirect:/success";
    }
}