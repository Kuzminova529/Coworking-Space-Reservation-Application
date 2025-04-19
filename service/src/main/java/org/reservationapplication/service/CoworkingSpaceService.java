package org.reservationapplication.service;

import org.reservationapplication.domain.model.CoworkingSpace;

import java.time.LocalDateTime;
import java.util.List;

public interface CoworkingSpaceService {

    void saveCoworkingSpaces(List<CoworkingSpace> coworkingSpaces);

    CoworkingSpace getCoworkingSpaceByIDForReservation(Long id);

    List<CoworkingSpace> getAllCoworkingSpace();

    List<CoworkingSpace> getActiveCoworkingSpace();

    CoworkingSpace getCoworkingSpaceByID(Long coworkingID);

    CoworkingSpace userAddCoworkingSpace(int typeChoice, double price);

    CoworkingSpace addCoworkingSpace(CoworkingSpace coworkingSpace);

    boolean removeCoworkingSpaceById(long id);

    boolean isTimeSlotAvailable(Long coworkingSpaceId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}