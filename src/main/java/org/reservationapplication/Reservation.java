package org.reservationapplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reservation {
    private static long nextId = 0L;
    private final long reservationID;
    private long coworkingSpaceID;
    private long customerID;
    private String reservationName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public Reservation(ReservationService generalReservation) {
        this.reservationID = nextId++;
        generalReservation.addGeneralReservation(this);
    }

    public long getReservationID() {
        return reservationID;
    }


    public void setCoworkingSpaceID(long coworkingSpaceID) {
        this.coworkingSpaceID = coworkingSpaceID;
    }

    public void setCustomerID(long customerID) {
        this.customerID = customerID;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }


    public void setStartReservationDateAndTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }


    public void setEndReservationDateAndTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        String formattedStartDateTime = startDateTime.format(formatter);
        String formattedEndDateTime = endDateTime.format(formatter);
        return "Reservation: " +
                "reservationID='" + reservationID + '\'' +
                ", coworkingSpaceID='" + coworkingSpaceID + '\'' +
                ", customerID='" + customerID + '\'' +
                ", reservationName='" + reservationName + '\'' +
                ", startReservationDateAdnTime=" + formattedStartDateTime +
                ", endReservationDateAdnTime=" + formattedEndDateTime;
    }
}
