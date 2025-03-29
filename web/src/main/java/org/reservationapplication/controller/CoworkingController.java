package org.reservationapplication.controller;

import org.checkerframework.checker.units.qual.A;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.service.CoworkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coworking-space")
public class CoworkingController {
    private final CoworkingSpaceService service;

    @Autowired
    public CoworkingController(@Qualifier("coworkingSpaceServiceImpl") CoworkingSpaceService service) {
        this.service = service;
    }

    @GetMapping()
    public List<CoworkingSpace> getAllSpaces() {
        return service.getAllCoworkingSpace();
    }

    @GetMapping("/{id}")
    public CoworkingSpace getSpaceById(@PathVariable("id") Long spaceId) {
        return service.getCoworkingSpaceByID(spaceId);
    }

    @GetMapping("/active")
    public List<CoworkingSpace> getActiveSpaces() {
        return service.getActiveCoworkingSpace();
    }

    @PostMapping
    public CoworkingSpace createSpace(@RequestBody CoworkingSpace space) {
        return service.addCoworkingSpace(space);
    }

    @PostMapping("/user")
    public CoworkingSpace userCreateSpace(@RequestBody int choice, @RequestBody double price) {
        return service.userAddCoworkingSpace(choice, price);
    }

    @DeleteMapping("/{id}")
    public boolean deleteSpace(@PathVariable Long id) {
        return service.removeCoworkingSpace(id);
    }

}
