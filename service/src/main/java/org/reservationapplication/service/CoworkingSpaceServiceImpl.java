package org.reservationapplication.service;

import org.reservationapplication.model.AvailabilityStatus;
import org.reservationapplication.model.CoworkingSpace;
import org.reservationapplication.repository.CoworkingSpaceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoworkingSpaceServiceImpl implements CoworkingSpaceService {
    private final CacheServiceCoworkingSpace cacheServiceCoworkingSpace = new CacheServiceCoworkingSpace();
    private final CoworkingSpaceRepository coworkingSpaceRepository = new CoworkingSpaceRepository();

    public CoworkingSpaceServiceImpl() {
        cacheServiceCoworkingSpace.removeAllCoworkingSpaces();
    }

    public CoworkingSpaceServiceImpl(List<CoworkingSpace> coworkingSpaces) {
        cacheServiceCoworkingSpace.removeAllCoworkingSpaces();
        for (CoworkingSpace cs : coworkingSpaces) {
                addCoworkingSpace(cs);
        }
    }

    public Optional<CoworkingSpace> getCoworkingSpaceByID(long id) {
        return coworkingSpaceRepository.getById(id);
    }

    public List<CoworkingSpace> getAllCoworkingSpace() {
        return cacheServiceCoworkingSpace.getAllCoworkingSpaces();
    }

    public void addCoworkingSpace(CoworkingSpace coworkingSpace) {
        cacheServiceCoworkingSpace.addCoworkingSpace(coworkingSpace);
    }

    public void removeCoworkingSpace(long id) {
        cacheServiceCoworkingSpace.removeCoworkingSpaceByID(id);
    }

    public List<CoworkingSpace> getAvailableCoworkingSpace() {
        List<CoworkingSpace> coworkingSpaces = cacheServiceCoworkingSpace.getAllCoworkingSpaces();
        List<CoworkingSpace> availableSpaces = new ArrayList<>();
        for (CoworkingSpace cs : coworkingSpaces) {
            if (cs.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE) {
                availableSpaces.add(cs);
            }
        }
        return availableSpaces;
    }
}
