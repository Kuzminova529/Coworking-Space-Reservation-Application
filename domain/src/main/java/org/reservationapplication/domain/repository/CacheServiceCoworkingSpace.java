package org.reservationapplication.domain.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.repository.SpringDataJPARepos.CoworkingSpaceRepositorySpring;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class CacheServiceCoworkingSpace implements CoworkingSpaceRepository{
    private final CoworkingSpaceRepositorySpring repository;

    private final Cache<String, List<CoworkingSpace>> cache;
    private final Cache<Long, CoworkingSpace> idCache;

    public CacheServiceCoworkingSpace(@Qualifier("coworkingSpaceRepositorySpring") CoworkingSpaceRepositorySpring repository) {
        this.repository = repository;
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES) // Clears cache every 10mins
                .maximumSize(100) // Coworking spaces limit
                .build();
        this.idCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(500)
                .build();
    }

    @Override
    public Optional<CoworkingSpace> getByIdOptional(Long id) {
        return Optional.ofNullable(idCache.get(id, key ->
                repository.findByIdOptional(key).orElse(null)
        ));
    }

    @Override
    public List<CoworkingSpace> findAll() {
        return cache.get("coworkings", key -> repository.getCoworkingSpaces());
    }

    @Override
    public void save(CoworkingSpace coworkingSpace) {
        repository.save(coworkingSpace);
        cache.invalidate("coworkings");
    }

    @Override
    public void saveAll(List<CoworkingSpace> coworkingSpace) {
        repository.saveAll(coworkingSpace);
        cache.invalidate("coworkings");
    }

    @Override
    public void updateStatus(Long id) {
        repository.updateStatus(id);
        cache.invalidate("coworkings");
    }

    public Cache<String, List<CoworkingSpace>> getCache() {
        return cache;
    }

    @Override
    public CoworkingSpace getCoworkingSpaceWithReservations(Long coworkingSpaceId) {
        return null;
    }
}