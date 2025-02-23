package org.reservationapplication.service;

import org.reservationapplication.model.Customer;
import org.reservationapplication.model.Reservation;

import java.util.List;

public interface ReservationService {
    public List<Reservation> getPersonalReservation();
    public void addPersonalReservation(Reservation reservation);
    public void addGeneralReservation(Reservation reservation);
    public boolean removePersonalReservationById(long id);
    public void removeFromGeneralReservationById(long id);
    public void userAddReservation(long id, String reservationName, String dateInput,
                                   String startTimeInput, String endTimeInput,
                                   Customer user, CoworkingSpaceServiceImpl coworkingSpaceService,
                                   ReservationServiceImpl reservationService);

    public void printGeneralReservation();
}
