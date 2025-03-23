package org.service;

import org.domain.model.CoworkingSpace;
import org.domain.repository.JPARepos.CoworkingSpaceRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoworkingSpaceServiceImpl implements CoworkingSpaceService {
    private final CacheServiceCoworkingSpace cacheServiceCoworkingSpace;
    private final CoworkingSpaceRepositoryJPA coworkingSpaceRepository;

    @Autowired
    public CoworkingSpaceServiceImpl(CacheServiceCoworkingSpace cacheServiceCoworkingSpace, CoworkingSpaceRepositoryJPA coworkingSpaceRepository) {
        this.cacheServiceCoworkingSpace = cacheServiceCoworkingSpace;
        this.coworkingSpaceRepository = coworkingSpaceRepository;
    }

    public CoworkingSpaceServiceImpl(List<CoworkingSpace> coworkingSpaces, CacheServiceCoworkingSpace cacheServiceCoworkingSpace, CoworkingSpaceRepositoryJPA coworkingSpaceRepository) {
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

    public List<CoworkingSpace> getAvailableCoworkingSpace() {
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


}
