package org.reservationapplication.repository;

public interface DataStorage<T> {
    void save(T data);
    T load();
}