package org.reservationapplication.domain.repository.SpringDataJPARepos;

import org.reservationapplication.domain.model.CoworkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoworkingSpaceRepositorySpring extends JpaRepository<CoworkingSpace, Long> {

    @Query("SELECT c FROM CoworkingSpace c WHERE c.id = :id")
    Optional<CoworkingSpace> getCoworkingSpaceById(@Param("id") Long id);

    @Query("SELECT c FROM CoworkingSpace c LEFT JOIN FETCH c.reservations WHERE c.id = :id")
    CoworkingSpace getCoworkingSpaceWithReservations(@Param("id") Long id);

    @Query("SELECT c FROM CoworkingSpace c WHERE c.isActive = true")
    List<CoworkingSpace> getCoworkingSpaces();
    @Modifying
    @Transactional
    @Query("UPDATE CoworkingSpace c SET c.isActive = false WHERE c.id = :id")
    void updateStatus(@Param("id") Long id);
}
