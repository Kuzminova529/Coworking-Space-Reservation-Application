package org.reservationapplication.service;

import org.reservationapplication.domain.dto.CoworkingSpaceDto;
import org.reservationapplication.domain.model.CoworkingSpace;

import java.time.LocalDateTime;
import java.util.List;

public interface CoworkingSpaceService {

    CoworkingSpaceDto toDto(CoworkingSpace space);

    CoworkingSpace toEntity(CoworkingSpaceDto dto);

    void saveCoworkingSpaces(List<CoworkingSpace> coworkingSpaces);

    List<CoworkingSpaceDto> getAllCoworkingSpace();

    List<CoworkingSpaceDto> getActiveCoworkingSpace();

    CoworkingSpaceDto getCoworkingSpaceByID(long coworkingID);

    CoworkingSpaceDto userAddCoworkingSpace(int typeChoice, double price);

    CoworkingSpaceDto addCoworkingSpace(CoworkingSpaceDto coworkingSpace);

    boolean removeCoworkingSpaceById(long id);

    boolean isTimeSlotAvailable(Long coworkingSpaceId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}