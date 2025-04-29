package org.reservationapplication.web.controller;


import org.reservationapplication.domain.model.User;
import org.reservationapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(@Qualifier("userService") UserService service) {
        this.service = service;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }
}
