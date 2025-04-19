package org.reservationapplication.domain.repository.SpringDataJPARepos;

import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.repository.ReservationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepositorySpring extends JpaRepository<Reservation, Long>, ReservationRepository {

    @Query("SELECT r FROM Reservation r WHERE r.id = :id")
    Optional<Reservation> getByIdOptional(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Reservation r SET r.isActive = false WHERE r.id = :id")
    void updateStatus(@Param("id") Long id);

    @Query("SELECT r FROM Reservation r WHERE r.userID = :userID")
    List<Reservation> readPersonalReservations(@Param("userID") Long userID);
}