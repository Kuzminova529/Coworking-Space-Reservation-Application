package org.reservationapplication.web.controller;

import org.reservationapplication.domain.dto.CoworkingSpaceDto;
import org.reservationapplication.service.CoworkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public List<CoworkingSpaceDto> getAllSpaces() {
        return service.getAllCoworkingSpace();
    }

    @GetMapping("/{id}")
    public CoworkingSpaceDto getSpaceById(@PathVariable("id") Long spaceId) {
        return service.getCoworkingSpaceByID(spaceId);
    }

    @PostMapping("/create")
    public CoworkingSpaceDto createSpace(@RequestBody CoworkingSpaceDto space) {
        return service.addCoworkingSpace(space);
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteSpace(@PathVariable Long id) {
        return service.removeCoworkingSpaceById(id);
    }

}
