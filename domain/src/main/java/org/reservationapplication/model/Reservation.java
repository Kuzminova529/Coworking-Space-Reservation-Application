package org.reservationapplication.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "reservations")
public class Reservation implements Comparable<Reservation>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "coworking_space_id", nullable = false)
    private Long coworkingSpaceID;

    @Column(name = "user_id", nullable = false)
    private Long userID;

    @Column(name = "reservation_name", length = 255, nullable = false)
    private String reservationName;

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;


    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
    public Reservation(){}

    public Long getID() {
        return id;
    }

    public void setID(long id) {
        this.id = id;
    }
    public Long getCoworkingSpaceID() {
        return coworkingSpaceID;
    }

    public void setCoworkingSpaceID(long coworkingSpaceID) {
        this.coworkingSpaceID = coworkingSpaceID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(long customerID) {
        this.userID = customerID;
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

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        String formattedStartDateTime = startDateTime.format(formatter);
        String formattedEndDateTime = endDateTime.format(formatter);
        return "Reservation: " +
                "reservationID='" + id + '\'' +
                ", coworkingSpaceID='" + coworkingSpaceID + '\'' +
                ", userID='" + userID + '\'' +
                ", reservationName='" + reservationName + '\'' +
                ", startReservationDateAndTime=" + formattedStartDateTime +
                ", endReservationDateAndTime=" + formattedEndDateTime;
    }

    @Override
    public int compareTo(Reservation other) {
        return this.startDateTime.compareTo(other.startDateTime);
    }
}