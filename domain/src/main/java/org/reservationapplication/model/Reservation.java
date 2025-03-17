package org.reservationapplication.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reservation implements Comparable<Reservation>{
    private long id;
    private long coworkingSpaceID;
    private long customerID;
    private String reservationName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public long getID() {
        return id;
    }

    public void setID(long id) {
        this.id = id;
    }
    public long getCoworkingSpaceID() {
        return coworkingSpaceID;
    }

    public void setCoworkingSpaceID(long coworkingSpaceID) {
        this.coworkingSpaceID = coworkingSpaceID;
    }

    public long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(long customerID) {
        this.customerID = customerID;
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
                ", customerID='" + customerID + '\'' +
                ", reservationName='" + reservationName + '\'' +
                ", startReservationDateAndTime=" + formattedStartDateTime +
                ", endReservationDateAndTime=" + formattedEndDateTime;
    }

    @Override
    public int compareTo(Reservation other) {
        return this.startDateTime.compareTo(other.startDateTime);
    }
}