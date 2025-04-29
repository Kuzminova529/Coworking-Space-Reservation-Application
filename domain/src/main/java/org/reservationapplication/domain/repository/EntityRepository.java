package org.reservationapplication.domain.repository;


import java.util.List;
import java.util.Optional;

public interface EntityRepository<T, K> {

    List<T> findAll();

    <S extends T> S save(S entity);

    Optional<T> findByIdCustom(K id);

    void updateStatus(K id);
}
