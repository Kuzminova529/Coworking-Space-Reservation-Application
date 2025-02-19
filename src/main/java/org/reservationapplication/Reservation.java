package org.reservationapplication;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Reservation {
    private static long nextId = 0L;
    private final long reservationID;
    private long coworkingSpaceID;
    private long customerID;
    private String reservationName;
    private Calendar startReservationDateAndTime = Calendar.getInstance();
    private Calendar endReservationDateAndTime = Calendar.getInstance();

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


    public void setStartReservationDateAndTime(Calendar startReservationDateAdnTime) {
        this.startReservationDateAndTime = startReservationDateAdnTime;
    }


    public void setEndReservationDateAndTime(Calendar endReservationDateAdnTime) {
        this.endReservationDateAndTime = endReservationDateAdnTime;
    }

    @Override
    public String toString() {
        Date startDate = startReservationDateAndTime.getTime();
        Date endDate = endReservationDateAndTime.getTime();
        return "Reservation: " +
                "reservationID='" + reservationID + '\'' +
                ", coworkingSpaceID='" + coworkingSpaceID + '\'' +
                ", reservationName='" + reservationName + '\'' +
                ", startReservationDateAdnTime=" + startDate +
                ", endReservationDateAdnTime=" + endDate;
    }
}
