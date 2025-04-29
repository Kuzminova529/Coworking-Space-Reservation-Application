package org.reservationapplication.domain.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.repository.SpringDataJPARepos.CoworkingSpaceRepositorySpring;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class CacheCoworkingSpaceRepository implements CoworkingSpaceRepository{
    private final CoworkingSpaceRepositorySpring repository;

    private final Cache<String, List<CoworkingSpace>> cache;
    private final Cache<Long, CoworkingSpace> idCache;

    public CacheCoworkingSpaceRepository(@Qualifier("coworkingSpaceRepositorySpring") CoworkingSpaceRepositorySpring repository) {
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
    public Optional<CoworkingSpace> findByIdCustom(Long id) {
        return Optional.ofNullable(idCache.get(id, key ->
                repository.findByIdCustom(key).orElse(null)
        ));
    }

    @Override
    public List<CoworkingSpace> findAll() {
        return cache.get("coworkings", key -> repository.getCoworkingSpaces());
    }

    @Override
    @Transactional
    public <S extends CoworkingSpace> S save(S coworkingSpace) {
        S returnedCoworking = repository.save(coworkingSpace);
        cache.invalidate("coworkings");
        return returnedCoworking;
    }

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
    public Optional<CoworkingSpace> getCoworkingSpaceWithReservations(Long coworkingSpaceId) {
        return repository.getCoworkingSpaceWithReservations(coworkingSpaceId);
    }
}