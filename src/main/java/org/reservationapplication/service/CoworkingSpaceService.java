package org.reservationapplication.service;

import org.reservationapplication.model.CoworkingSpace;

import java.util.List;

public interface CoworkingSpaceService {

    public List<CoworkingSpace> getAllCoworkingSpace();

    public void addCoworkingSpace(CoworkingSpace coworkingSpace);

    public void removeCoworkingSpace(long id);

    public List<CoworkingSpace> loadAvailableCoworkingSpace();

}