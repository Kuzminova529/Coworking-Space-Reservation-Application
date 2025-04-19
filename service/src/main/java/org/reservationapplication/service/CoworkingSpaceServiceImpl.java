package org.reservationapplication.service;

import org.reservationapplication.domain.exeption.BusinessException;
import org.reservationapplication.domain.exeption.CoworkingSpaceNotFoundException;
import org.reservationapplication.domain.exeption.DatabaseException;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.model.CoworkingSpaceType;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.repository.CacheServiceCoworkingSpace;
import org.reservationapplication.domain.repository.SpringDataJPARepos.CoworkingSpaceRepositorySpring;
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

    @Autowired
    public CoworkingSpaceServiceImpl( @Qualifier("cacheServiceCoworkingSpace") CacheServiceCoworkingSpace cacheServiceCoworkingSpace) {
        this.cacheServiceCoworkingSpace = cacheServiceCoworkingSpace;
    }

    @Override
    public CoworkingSpace getCoworkingSpaceByID(Long id) {
        try {
            Optional<CoworkingSpace> optCoworkingSpace = cacheServiceCoworkingSpace.getByIdOptional(id);
            if (optCoworkingSpace.isPresent()) {
                CoworkingSpace coworkingSpace = optCoworkingSpace.get();
                return coworkingSpace;
            }
            else {
                throw new BusinessException("No coworking space with this id");
            }
        } catch (DatabaseException e) {
            throw new BusinessException("Failed to find coworking space by id");
        }
    }

    @Override
    public CoworkingSpace getCoworkingSpaceByIDForReservation(Long id) {
        try {
            Optional<CoworkingSpace> optCoworkingSpace = cacheServiceCoworkingSpace.getByIdOptional(id);
            if (optCoworkingSpace.isPresent()) {
                CoworkingSpace coworkingSpace = optCoworkingSpace.get();
                return coworkingSpace;
            }
            else {
                throw new BusinessException("No coworking space with this id");
            }
        } catch (DatabaseException e){
            throw new BusinessException("Failed to find coworking space by id");
        }
    }

    @Override
    public List<CoworkingSpace> getAllCoworkingSpace() {
        try {
            return cacheServiceCoworkingSpace.findAll();
        } catch (DatabaseException e){
            throw new BusinessException("Failed to get all coworking spaces");
        }
    }

    @Override
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
        cacheServiceCoworkingSpace.save(coworkingSpace);
        return coworkingSpace;
    }

    @Override
    public void saveCoworkingSpaces(List<CoworkingSpace> coworkingSpaces) {
        try {
            cacheServiceCoworkingSpace.saveAll(coworkingSpaces);
        } catch (DatabaseException e){
            throw new BusinessException("Failed to save coworking spaces");
        }
    }

    @Override
    public boolean removeCoworkingSpaceById(long id) {
        try {
            CoworkingSpace coworkingSpace = getCoworkingSpaceByID(id);
        } catch (BusinessException e) {
            throw new BusinessException("Failed to remove coworking space");
        }
        try {
            cacheServiceCoworkingSpace.updateStatus(id);
            return true;
        } catch (DatabaseException e){
            throw new BusinessException("Failed to remove coworking space by id");
        }
    }

    @Override
    public List<CoworkingSpace> getActiveCoworkingSpace() {
        try {
            List<CoworkingSpace> coworkingSpaces = cacheServiceCoworkingSpace.findAll();
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
            CoworkingSpace coworkingSpace = cacheServiceCoworkingSpace.getCoworkingSpaceWithReservations(coworkingSpaceId);
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
