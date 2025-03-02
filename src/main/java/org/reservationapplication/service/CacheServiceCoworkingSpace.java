package org.reservationapplication.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.reservationapplication.model.CoworkingSpace;
import org.reservationapplication.repository.CoworkingSpaceRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CacheServiceCoworkingSpace {
    private final CoworkingSpaceRepository repository = new CoworkingSpaceRepository();
    private final Cache<String, List<CoworkingSpace>> cache;

    public CacheServiceCoworkingSpace() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES) // Clears cache every 10mins
                .maximumSize(100) // Coworking spaces limit
                .build();
    }

    public List<CoworkingSpace> getAllCoworkingSpaces() {
        return cache.get("coworkings", key -> repository.read());
    }

    public void addCoworkingSpace(CoworkingSpace coworkingSpace) {
        repository.add(coworkingSpace);
        cache.invalidate("coworkings");
    }

    public void removeCoworkingSpaceByID(long id) {
        repository.deleteByID(id);
        cache.invalidate("coworkings");
    }

    public void removeAllCoworkingSpaces() {
        repository.deleteAll();
        cache.invalidateAll();
    }
}