package org.reservationapplication.web.controller;

import org.reservationapplication.domain.model.User;
import org.reservationapplication.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // шаблон регистрации
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        userService.registerNewUser(user);
        return "redirect:/login";
    }
}
