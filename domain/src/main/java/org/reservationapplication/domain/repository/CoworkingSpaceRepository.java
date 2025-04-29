package org.reservationapplication.domain.repository;

import org.reservationapplication.domain.model.CoworkingSpace;

import java.util.Optional;

public interface CoworkingSpaceRepository extends EntityRepository<CoworkingSpace, Long> {
    Optional<CoworkingSpace> getCoworkingSpaceWithReservations(Long coworkingSpaceId);
}
