package org.reservationapplication.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "coworking_space_id", nullable = false)
    @JsonBackReference
    private CoworkingSpace coworkingSpace;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "reservation_name", length = 255, nullable = false)
    private String reservationName;

    @Column(name = "start_datetime", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDateTime;

    public Reservation(){}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getReservationName() {
        return reservationName;
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
                ", userId=" + userId +
                ", reservationName='" + reservationName + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                "is_actve="+super.getActive() +
                '}';
    }
}