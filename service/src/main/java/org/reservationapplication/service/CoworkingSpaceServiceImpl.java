package org.reservationapplication.service;

import org.checkerframework.checker.units.qual.C;
import org.reservationapplication.domain.exeption.CoworkingSpaceNotFoundException;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.repository.CoworkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoworkingSpaceServiceImpl implements CoworkingSpaceService {
    private final CacheServiceCoworkingSpace cacheServiceCoworkingSpace;
    private final CoworkingSpaceRepository coworkingSpaceRepository;

    @Autowired
    public CoworkingSpaceServiceImpl(CacheServiceCoworkingSpace cacheServiceCoworkingSpace, @Qualifier("jpaCoworkingSpaceRepository") CoworkingSpaceRepository coworkingSpaceRepository) {
        this.cacheServiceCoworkingSpace = cacheServiceCoworkingSpace;
        this.coworkingSpaceRepository = coworkingSpaceRepository;
    }

    public CoworkingSpaceServiceImpl(List<CoworkingSpace> coworkingSpaces, CacheServiceCoworkingSpace cacheServiceCoworkingSpace, CoworkingSpaceRepository coworkingSpaceRepository) {
        this.cacheServiceCoworkingSpace = cacheServiceCoworkingSpace;
        this.coworkingSpaceRepository = coworkingSpaceRepository;
        if (coworkingSpaces != null) {
            saveCoworkingSpaces(coworkingSpaces);
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

    public void saveCoworkingSpaces(List<CoworkingSpace> coworkingSpaces) {
        cacheServiceCoworkingSpace.saveCoworkingSpaces(coworkingSpaces);
    }

    public void removeCoworkingSpace(long id) {
        cacheServiceCoworkingSpace.removeCoworkingSpaceByID(id);
    }

    public List<CoworkingSpace> getActiveCoworkingSpace() {
        List<CoworkingSpace> coworkingSpaces = cacheServiceCoworkingSpace.getAllCoworkingSpaces();
        List<CoworkingSpace> availableSpaces = new ArrayList<>();
        if (coworkingSpaces != null) {
            for (CoworkingSpace cs : coworkingSpaces) {
                if (cs.getActive()) {
                    availableSpaces.add(cs);
                }
            }
        }
        return availableSpaces;
    }

    @Override
    public boolean isTimeSlotAvailable(Long coworkingSpaceId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        CoworkingSpace coworkingSpace = coworkingSpaceRepository.getCoworkingSpaceWithReservations(coworkingSpaceId);
        if (coworkingSpace == null) {
            throw new CoworkingSpaceNotFoundException(coworkingSpaceId,404);
        }
        for (Reservation reservation : coworkingSpace.getReservations()) {
            if ((startDateTime.isBefore(reservation.getEndDateTime()) && endDateTime.isAfter(reservation.getStartDateTime())) ||
                    (startDateTime.equals(reservation.getStartDateTime()) || endDateTime.equals(reservation.getEndDateTime()))) {
                return false;
            }
        }
        return true;
    }

}
