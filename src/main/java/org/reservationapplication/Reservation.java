package org.reservationapplication;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Reservation {
    private final String reservationID = UUID.randomUUID().toString();
    private String coworkingSpaceID;
    private String reservationName;
    private Calendar startReservationDateAndTime = Calendar.getInstance();
    private Calendar endReservationDateAndTime = Calendar.getInstance();

    public Reservation(){
        GeneralReservationList.addGeneralReservationList(this);
    }

    public String getReservationID() {
        return reservationID;
    }


    public void setCoworkingSpaceID(String coworkingSpaceID) {
        this.coworkingSpaceID = coworkingSpaceID;
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
