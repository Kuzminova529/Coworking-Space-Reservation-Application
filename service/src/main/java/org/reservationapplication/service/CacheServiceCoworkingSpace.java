package org.reservationapplication.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.repository.CoworkingSpaceRepository;
import org.reservationapplication.domain.repository.SpringDataJPARepos.CoworkingSpaceRepositorySpring;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceCoworkingSpace {
    private final CoworkingSpaceRepositorySpring repository;
    private final Cache<String, List<CoworkingSpace>> cache;

    public CacheServiceCoworkingSpace(@Qualifier("coworkingSpaceRepositorySpring") CoworkingSpaceRepositorySpring repository) {
        this.repository = repository;
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES) // Clears cache every 10mins
                .maximumSize(100) // Coworking spaces limit
                .build();
    }

    public List<CoworkingSpace> findById(Long id) {
        return cache.get("coworkings", key -> {
            Optional<CoworkingSpace> coworkingSpace = repository.getCoworkingSpaceById(id);
            return coworkingSpace.map(Collections::singletonList).orElse(Collections.emptyList());
        });    }

    public List<CoworkingSpace> getAllCoworkingSpaces() {
        return cache.get("coworkings", key -> repository.getCoworkingSpaces());
    }

    public void addCoworkingSpace(CoworkingSpace coworkingSpace) {
        repository.save(coworkingSpace);
        cache.invalidate("coworkings");
    }

    public void saveCoworkingSpaces(List<CoworkingSpace> coworkingSpace) {
        repository.saveAll(coworkingSpace);
        cache.invalidate("coworkings");
    }
    public void removeCoworkingSpaceByID(long id) {
        repository.updateStatus(id);
        cache.invalidate("coworkings");
    }

    public Cache<String, List<CoworkingSpace>> getCache() {
        return cache;
    }


}