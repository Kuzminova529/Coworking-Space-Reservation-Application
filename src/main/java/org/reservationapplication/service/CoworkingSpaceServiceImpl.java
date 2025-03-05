package org.reservationapplication.service;

import org.reservationapplication.exeption.CoworkingSpaceNotFoundException;
import org.reservationapplication.model.AvailabilityStatus;
import org.reservationapplication.model.CoworkingSpace;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoworkingSpaceServiceImpl implements CoworkingSpaceService {
    private CacheServiceCoworkingSpace cacheServiceCoworkingSpace = new CacheServiceCoworkingSpace();

    public CoworkingSpaceServiceImpl() {
    }

    public CoworkingSpaceServiceImpl(List<CoworkingSpace> coworkingSpaces) {
        cacheServiceCoworkingSpace.removeAllCoworkingSpaces();
        for (CoworkingSpace cs : coworkingSpaces) {
                addCoworkingSpace(cs);
        }
    }

    public CoworkingSpace getCoworkingSpaceByID(long id) {
        List<CoworkingSpace> coworkingSpaces = cacheServiceCoworkingSpace.getAllCoworkingSpaces();
        for (CoworkingSpace cs : coworkingSpaces) {
            if (cs.getID() == id) {
                return cs;
            }
        }
        throw new CoworkingSpaceNotFoundException(id);
    }

    public Optional<List<CoworkingSpace>> getAllCoworkingSpace() {
        return Optional.ofNullable(cacheServiceCoworkingSpace.getAllCoworkingSpaces());
    }

    public void addCoworkingSpace(CoworkingSpace coworkingSpace) {
        cacheServiceCoworkingSpace.addCoworkingSpace(coworkingSpace);
    }

    public void removeCoworkingSpace(long id) {
        cacheServiceCoworkingSpace.removeCoworkingSpaceByID(id);
    }

    public Optional<List<CoworkingSpace>> getAvailableCoworkingSpace() {
        List<CoworkingSpace> coworkingSpaces = cacheServiceCoworkingSpace.getAllCoworkingSpaces();
        List<CoworkingSpace> availableSpaces = new ArrayList<>();
        for (CoworkingSpace cs : coworkingSpaces) {
            if (cs.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE) {
                availableSpaces.add(cs);
            }
        }
        return Optional.ofNullable(availableSpaces);
    }
}
