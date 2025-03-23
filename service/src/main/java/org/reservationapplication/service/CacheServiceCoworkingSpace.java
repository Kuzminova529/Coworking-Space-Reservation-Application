package org.reservationapplication.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.repository.EntityRepository;
import org.reservationapplication.domain.repository.JPARepos.CoworkingSpaceRepositoryJPA;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceCoworkingSpace {
    private final EntityRepository<CoworkingSpace, Long> repository;
    private final Cache<String, List<CoworkingSpace>> cache;

    public CacheServiceCoworkingSpace(EntityRepository<CoworkingSpace, Long> repository) {
        this.repository = repository;
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES) // Clears cache every 10mins
                .maximumSize(100) // Coworking spaces limit
                .build();
    }

    public List<CoworkingSpace> getAllCoworkingSpaces() {
        return cache.get("coworkings", key -> repository.read());
    }

    public void addCoworkingSpace(CoworkingSpace coworkingSpace) {
        repository.create(coworkingSpace);
        cache.invalidate("coworkings");
    }

    public void saveCoworkingSpaces(List<CoworkingSpace> coworkingSpace) {
        repository.save(coworkingSpace);
        cache.invalidate("coworkings");
    }
    public void removeCoworkingSpaceByID(long id) {
        repository.updateStatus(id);
        cache.invalidate("coworkings");
    }

    public void removeAllCoworkingSpaces() {
        cache.invalidateAll();
    }

    public Cache<String, List<CoworkingSpace>> getCache() {
        return cache;
    }


}