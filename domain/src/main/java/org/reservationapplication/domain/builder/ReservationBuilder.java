package org.reservationapplication.domain.builder;

import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.model.Reservation;

import java.time.LocalDateTime;

public class ReservationBuilder {
    private CoworkingSpace coworkingSpace;
    private Long userId;
    private String reservationName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private boolean isActive;

    public ReservationBuilder setCoworkingSpace(CoworkingSpace coworkingSpace) {
        this.coworkingSpace = coworkingSpace;
        return this;
    }

    public ReservationBuilder setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public ReservationBuilder setReservationName(String reservationName) {
        this.reservationName = reservationName;
        return this;
    }

    public ReservationBuilder setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public ReservationBuilder setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }

    public ReservationBuilder setActive(boolean active) {
        this.isActive = active;
        return this;
    }


    public Reservation build() {
        Reservation reservation = new Reservation();
        reservation.setCoworkingSpace(coworkingSpace);
        reservation.setUserID(userId);
        reservation.setReservationName(reservationName);
        reservation.setStartDateTime(startDateTime);
        reservation.setEndDateTime(endDateTime);
        reservation.setActive(isActive);
        return reservation;
    }
}