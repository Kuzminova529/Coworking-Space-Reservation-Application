package org.reservationapplication.repository;

import java.util.List;

public interface EntityRepository<T, K> {

    void save(List<T> items);

    List<T> read();

    void create(T item);

    void deleteByID(K id);

    void deleteAll();

}
