package org.reservationapplication.service;

import org.reservationapplication.domain.model.CoworkingSpace;

import java.time.LocalDateTime;
import java.util.List;

public interface CoworkingSpaceService {

    void saveCoworkingSpaces(List<CoworkingSpace> coworkingSpaces);

    List<CoworkingSpace> getAllCoworkingSpace();

    List<CoworkingSpace> getActiveCoworkingSpace();

    CoworkingSpace getCoworkingSpaceByID(Long coworkingID);

    CoworkingSpace addCoworkingSpace(CoworkingSpace coworkingSpace);

    boolean removeCoworkingSpace(long id);
}