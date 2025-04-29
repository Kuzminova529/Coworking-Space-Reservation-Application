package org.reservationapplication.service;

import org.reservationapplication.domain.exeption.BusinessException;
import org.reservationapplication.domain.exeption.CoworkingSpaceNotFoundException;
import org.reservationapplication.domain.exeption.DatabaseException;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.repository.CacheCoworkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoworkingSpaceServiceImpl implements CoworkingSpaceService {
    private final CacheCoworkingSpaceRepository cacheCoworkingSpaceRepository;

    @Autowired
    public CoworkingSpaceServiceImpl( @Qualifier("cacheCoworkingSpaceRepository") CacheCoworkingSpaceRepository cacheCoworkingSpaceRepository) {
        this.cacheCoworkingSpaceRepository = cacheCoworkingSpaceRepository;
    }

    @Override
    public CoworkingSpace getCoworkingSpaceByID(Long id) {
        try {
            Optional<CoworkingSpace> optCoworkingSpace = cacheCoworkingSpaceRepository.findByIdCustom(id);
            if (optCoworkingSpace.isPresent()) {
                CoworkingSpace coworkingSpace = optCoworkingSpace.get();
                return coworkingSpace;
            }
            else {
                throw new BusinessException("No coworking space with this id");
            }
        } catch (DatabaseException e) {
            throw new BusinessException("Failed to find coworking space by id", e);
        }
    }

    @Override
    public List<CoworkingSpace> getAllCoworkingSpace() {
        try {
            return cacheCoworkingSpaceRepository.findAll();
        } catch (DatabaseException e){
            throw new BusinessException("Failed to get all coworking spaces", e);
        }
    }

    @Override
    public CoworkingSpace addCoworkingSpace(CoworkingSpace coworkingSpace) {
        cacheCoworkingSpaceRepository.save(coworkingSpace);
        return coworkingSpace;
    }

    @Override
    public void saveCoworkingSpaces(List<CoworkingSpace> coworkingSpaces) {
        try {
            cacheCoworkingSpaceRepository.saveAll(coworkingSpaces);
        } catch (DatabaseException e){
            throw new BusinessException("Failed to save coworking spaces", e);
        }
    }

    @Override
    public boolean removeCoworkingSpace(long id) {
        try {
            cacheCoworkingSpaceRepository.updateStatus(id);
            return true;
        } catch (DatabaseException e){
            throw new BusinessException("Failed to remove coworking space by id", e);
        }
    }

    @Override
    public List<CoworkingSpace> getActiveCoworkingSpace() {
        try {
            List<CoworkingSpace> coworkingSpaces = cacheCoworkingSpaceRepository.findAll();
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
            throw new BusinessException("Failed to get active coworking spaces", e);
        }
    }
}
