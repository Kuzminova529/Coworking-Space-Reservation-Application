package org.reservationapplication.service;

import org.checkerframework.checker.units.qual.C;
import org.reservationapplication.domain.exeption.BusinessException;
import org.reservationapplication.domain.exeption.CoworkingSpaceNotFoundException;
import org.reservationapplication.domain.exeption.DatabaseException;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.model.CoworkingSpaceType;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.repository.CoworkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("coworkingSpaceServiceImpl")
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

    public CoworkingSpace getCoworkingSpaceByID(long id) {
        try {
            Optional<CoworkingSpace> optionalCoworkingSpace = coworkingSpaceRepository.getById(id);
            if (optionalCoworkingSpace.isPresent()) {
                return optionalCoworkingSpace.get();
            }
            else {
                throw new BusinessException("Failed to find coworking space by id");
            }
        } catch (DatabaseException e){
            throw new BusinessException("Failed to find coworking space by id");
        }
    }

    public List<CoworkingSpace> getAllCoworkingSpace() {
        try {
            return cacheServiceCoworkingSpace.getAllCoworkingSpaces();
        } catch (DatabaseException e){
            throw new BusinessException("Failed to get all coworking spaces");
        }
    }

    public CoworkingSpace userAddCoworkingSpace(int typeChoice, double price) {
        try {
            CoworkingSpace coworkingSpace = new CoworkingSpace();

            switch (typeChoice) {
                case 1:
                    coworkingSpace.setType(CoworkingSpaceType.OPENSPACE);
                    break;
                case 2:
                    coworkingSpace.setType(CoworkingSpaceType.PRIVATE);
                    break;
                case 3:
                    coworkingSpace.setType(CoworkingSpaceType.ROOM);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid coworking space type choice: " + typeChoice);
            }

            coworkingSpace.setPrice(price);

            coworkingSpace.setActive(true);

            addCoworkingSpace(coworkingSpace);
            return coworkingSpace;
        } catch (DatabaseException e){
            throw new BusinessException("Failed to add coworking space");
        }
    }

    @Override
    public CoworkingSpace addCoworkingSpace(CoworkingSpace coworkingSpace) {
        cacheServiceCoworkingSpace.addCoworkingSpace(coworkingSpace);
        return coworkingSpace;
    }

    public void saveCoworkingSpaces(List<CoworkingSpace> coworkingSpaces) {
        try {
            cacheServiceCoworkingSpace.saveCoworkingSpaces(coworkingSpaces);
        } catch (DatabaseException e){
            throw new BusinessException("Failed to save coworking spaces");
        }
    }

    public boolean removeCoworkingSpace(long id) {
        try {
            cacheServiceCoworkingSpace.removeCoworkingSpaceByID(id);
            return true;
        } catch (DatabaseException e){
            throw new BusinessException("Failed to remove coworking space by id");
        }
    }

    public List<CoworkingSpace> getActiveCoworkingSpace() {
        try {
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
        } catch (DatabaseException e){
            throw new BusinessException("Failed to get active coworking spaces");
        }
    }

    @Override
    public boolean isTimeSlotAvailable(Long coworkingSpaceId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        try {
            CoworkingSpace coworkingSpace = coworkingSpaceRepository.getCoworkingSpaceWithReservations(coworkingSpaceId);
            if (coworkingSpace == null) {
                throw new CoworkingSpaceNotFoundException(coworkingSpaceId, 404);
            }
            for (Reservation reservation : coworkingSpace.getReservations()) {
                if ((startDateTime.isBefore(reservation.getEndDateTime()) && endDateTime.isAfter(reservation.getStartDateTime())) ||
                        (startDateTime.equals(reservation.getStartDateTime()) || endDateTime.equals(reservation.getEndDateTime()))) {
                    return false;
                }
            }
            return true;
        } catch (DatabaseException e){
            throw new BusinessException("Failed to check if time slots are available");
        }
    }
}
