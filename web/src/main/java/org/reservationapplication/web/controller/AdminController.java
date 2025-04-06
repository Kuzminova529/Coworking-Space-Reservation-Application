package org.reservationapplication.web.controller;


import org.reservationapplication.domain.dto.CoworkingSpaceDto;
import org.reservationapplication.domain.dto.ReservationDto;
import org.reservationapplication.service.CoworkingSpaceService;
import org.reservationapplication.service.ReservationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    CoworkingSpaceService coworkingSpaceService;
    ReservationService reservationService;

    public AdminController(@Qualifier("coworkingSpaceServiceImpl") CoworkingSpaceService coworkingSpaceService,
                           @Qualifier("reservationServiceImpl") ReservationService reservationService) {
        this.coworkingSpaceService = coworkingSpaceService;
        this.reservationService = reservationService;
    }

    @GetMapping("/view-coworkings")
    @ResponseBody
    public List<CoworkingSpaceDto> getAllCoworkingSpaces() {
        return coworkingSpaceService.getAllCoworkingSpace();
    }

    @GetMapping("/view-reservations")
    @ResponseBody
    public List<ReservationDto> getAllReservations() {
        return reservationService.getAllReservation();
    }

    @GetMapping("/add-coworking")
    public String showAddCoworkingForm(Model model) {
        model.addAttribute("coworkingSpace", new CoworkingSpaceDto());
        return "add-coworking";
    }

    @PostMapping("/add-coworking")
    public String addCoworkingSpace(@ModelAttribute("coworkingSpace") CoworkingSpaceDto coworkingSpaceDto) {
        coworkingSpaceDto.setActive(true);
        coworkingSpaceService.addCoworkingSpace(coworkingSpaceDto);
        return "redirect:/success";
    }

    @GetMapping("/remove-coworking")
    public String showRemoveCoworkingForm(Model model) {
        model.addAttribute("coworkingSpace", new CoworkingSpaceDto());
        return "remove-coworking";
    }

    @PostMapping("/remove-coworking")
    public String removeCoworkingSpace(@ModelAttribute("coworkingSpace") CoworkingSpaceDto coworkingSpaceDto) {
        coworkingSpaceService.removeCoworkingSpaceById(coworkingSpaceDto.getId());
        return "redirect:/success";
    }
}
