package org.reservationapplication.web.controller;

import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.web.dto.CoworkingSpaceDto;
import org.reservationapplication.service.CoworkingSpaceService;
import org.reservationapplication.web.mapper.DTOCoworkingSpaceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/coworking-space")
public class CoworkingController {
    private final CoworkingSpaceService service;
    private final DTOCoworkingSpaceMapper mapper;

    @Autowired
    public CoworkingController(@Qualifier("coworkingSpaceServiceImpl") CoworkingSpaceService service,
                               @Qualifier("DTOCoworkingSpaceMapper") DTOCoworkingSpaceMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Secured({"ROLE_CUSTOMER", "ROLE_ADMIN"})
    @GetMapping()
    public List<CoworkingSpaceDto> getAllSpaces() {
        List<CoworkingSpaceDto> dtos = new ArrayList<>();
        for(CoworkingSpace cs : service.getAllCoworkingSpace()){
            dtos.add(mapper.toDto(cs));
        }
        return dtos;
    }

    @Secured({"ROLE_CUSTOMER", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    public CoworkingSpaceDto getSpaceById(@PathVariable("id") Long spaceId) {
        return mapper.toDto(service.getCoworkingSpaceByID(spaceId));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/create")
    public CoworkingSpaceDto createSpace(@RequestBody CoworkingSpaceDto space) {
        return mapper.toDto(service.addCoworkingSpace(mapper.toEntity(space)));
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/delete/{id}")
    public boolean deleteSpace(@PathVariable Long id) {
        return service.removeCoworkingSpace(id);
    }
}
