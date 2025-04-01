package org.reservationapplication.domain.dto;

import java.time.LocalDateTime;

public class ReservationDto {
    private Long id;
    private CoworkingSpaceDto coworkingSpace;
    private Long userID;
    private String reservationName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private boolean active;

    public ReservationDto() {}

    public ReservationDto(Long id, CoworkingSpaceDto coworkingSpace, Long userID, String reservationName, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean active) {
        this.id = id;
        this.coworkingSpace = coworkingSpace;
        this.userID = userID;
        this.reservationName = reservationName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CoworkingSpaceDto getCoworkingSpace() {
        return coworkingSpace;
    }

    public void setCoworkingSpace(CoworkingSpaceDto coworkingSpace) {
        this.coworkingSpace = coworkingSpace;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
