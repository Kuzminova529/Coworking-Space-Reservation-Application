package org.reservationapplication.service;

import org.reservationapplication.domain.model.CoworkingSpace;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CoworkingSpaceService {

    List<CoworkingSpace> getAllCoworkingSpace();

    void addCoworkingSpace(int typeChoice, double price);

    void removeCoworkingSpace(long id);

    List<CoworkingSpace> getActiveCoworkingSpace();

    Optional<CoworkingSpace> getCoworkingSpaceByID(long coworkingID);

    boolean isTimeSlotAvailable(Long coworkingSpaceId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}