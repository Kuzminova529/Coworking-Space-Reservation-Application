package org.reservationapplication.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ReservationDto {
    private Long id;
    private Long coworkingSpaceId;
    private Long userId;
    private String reservationName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDateTime;
    private boolean active;

    public ReservationDto() {}

    public ReservationDto(Long id, Long coworkingSpaceId, Long userId, String reservationName, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean active) {
        this.id = id;
        this.coworkingSpaceId = coworkingSpaceId;
        this.userId = userId;
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

    public Long getCoworkingSpaceId() {
        return coworkingSpaceId;
    }

    public void setCoworkingSpaceId(Long coworkingSpaceId) {
        this.coworkingSpaceId = coworkingSpaceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userID) {
        this.userId = userID;
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
