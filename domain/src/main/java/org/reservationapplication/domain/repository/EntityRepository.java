package org.reservationapplication.domain.repository;


import java.util.List;
import java.util.Optional;

public interface EntityRepository<T, K> {

    void saveAll(List<T> items);

    List<T> findAll();

    void save(T item);

    Optional<T> getByIdOptional(K id);

    void updateStatus(K id);
}
