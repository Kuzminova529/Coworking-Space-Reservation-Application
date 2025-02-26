package org.reservationapplication.service;

import org.reservationapplication.model.Customer;
import org.reservationapplication.model.Reservation;

import java.util.List;

public interface ReservationService {
    public List<Reservation> getAllReservation();

    public void addReservation(Reservation reservation);

    public boolean removeReservationById(long id);

    public boolean userAddReservation(long id, String reservationName, String dateInput,
                                      String startTimeInput, String endTimeInput,
                                      Customer user, CoworkingSpaceServiceImpl coworkingSpaceService,
                                      ReservationServiceImpl reservationService);

}