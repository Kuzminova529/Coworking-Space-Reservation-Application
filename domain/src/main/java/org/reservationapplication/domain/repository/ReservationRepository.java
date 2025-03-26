package org.reservationapplication.domain.repository;

import org.reservationapplication.domain.model.Reservation;

import java.util.List;

public interface ReservationRepository extends EntityRepository<Reservation, Long>{
    List<Reservation> readPersonalReservations(Long userID);
}
