package org.reservationapplication.domain.repository;

public interface DataStorage<T> {
    void save(T data);
    T load();
}