package org.reservationapplication.service;

import org.reservationapplication.domain.model.CoworkingSpace;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CoworkingSpaceService {

    List<CoworkingSpace> getAllCoworkingSpace();


    List<CoworkingSpace> getActiveCoworkingSpace();

    CoworkingSpace getCoworkingSpaceByID(long coworkingID);

    CoworkingSpace userAddCoworkingSpace(int typeChoice, double price);

    CoworkingSpace addCoworkingSpace(CoworkingSpace coworkingSpace);

    boolean removeCoworkingSpace(long id);

    boolean isTimeSlotAvailable(Long coworkingSpaceId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}