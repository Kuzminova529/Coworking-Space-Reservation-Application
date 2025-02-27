package org.reservationapplication.service;

import org.reservationapplication.exeption.CoworkingSpaceNotFoundException;
import org.reservationapplication.model.AvailabilityStatus;
import org.reservationapplication.model.CoworkingSpace;

import java.util.ArrayList;
import java.util.List;

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

    public List<CoworkingSpace> getAllCoworkingSpace() {
        return cacheServiceCoworkingSpace.getAllCoworkingSpaces();
    }

    public void addCoworkingSpace(CoworkingSpace coworkingSpace) {
        cacheServiceCoworkingSpace.addCoworkingSpace(coworkingSpace);
    }

    public void removeCoworkingSpace(long id) {
        cacheServiceCoworkingSpace.removeCoworkingSpaceByID(id);
    }

    public List<CoworkingSpace> loadAvailableCoworkingSpace() {
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
