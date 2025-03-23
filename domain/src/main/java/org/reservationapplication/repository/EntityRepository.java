package org.reservationapplication.repository;

import java.util.List;
import java.util.Optional;

public interface EntityRepository<T, K> {

    void save(List<T> items);

    List<T> read();

    void create(T item);

    Optional<T> getById(K id);


}
