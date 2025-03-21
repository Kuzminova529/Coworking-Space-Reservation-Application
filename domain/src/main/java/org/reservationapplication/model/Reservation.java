package org.reservationapplication.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation extends EntityModel {

    @ManyToOne
    @JoinColumn(name = "coworking_space_id", nullable = false)
    private CoworkingSpace coworkingSpace;

    @Column(name = "user_id", nullable = false)
    private Long userID;

    @Column(name = "reservation_name", length = 255, nullable = false)
    private String reservationName;

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDateTime;

    public Reservation(){}

    public Long getUserID() {
        return userID;
    }

    public void setUserID(long customerID) {
        this.userID = customerID;
    }

    public String getReservationName() {
        return reservationName;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public CoworkingSpace getCoworkingSpace() {
        return coworkingSpace;
    }

    public void setCoworkingSpace(CoworkingSpace coworkingSpace) {
        this.coworkingSpace = coworkingSpace;
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

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + super.getId() +
                ", coworkingSpace=" + coworkingSpace +
                ", userID=" + userID +
                ", reservationName='" + reservationName + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime + '}';
    }

}