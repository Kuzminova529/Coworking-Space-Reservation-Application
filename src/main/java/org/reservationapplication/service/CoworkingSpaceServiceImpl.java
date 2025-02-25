package org.reservationapplication.service;

import org.reservationapplication.model.AvailabilityStatus;
import org.reservationapplication.model.CoworkingSpace;
import org.reservationapplication.repository.CoworkingSpaceRepository;

import java.util.ArrayList;
import java.util.List;

public class CoworkingSpaceServiceImpl implements CoworkingSpaceService {
    private CoworkingSpaceRepository repository;

    public CoworkingSpaceServiceImpl() {
        this.repository = new CoworkingSpaceRepository();
    }

    public List<CoworkingSpace> getAllCoworkingSpace() {
        return repository.readCoworkingSpace();
    }

    public void addCoworkingSpace(CoworkingSpace coworkingSpace) {
        repository.addCoworkingSpace(coworkingSpace);
    }

    public void removeCoworkingSpace(long id) {
        repository.deleteCoworkingSpaceByID(id);
    }

    public List<CoworkingSpace> loadAvailableCoworkingSpace() {
        List<CoworkingSpace> coworkingSpaces = repository.readCoworkingSpace();
        List<CoworkingSpace> availableSpaces = new ArrayList<>();
        for (CoworkingSpace cs : coworkingSpaces) {
            if (cs.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE) {
                availableSpaces.add(cs);
            }
        }
        return availableSpaces;
    }
}