package org.reservationapplication.domain.repository;

import org.reservationapplication.domain.model.CoworkingSpace;

public interface CoworkingSpaceRepository extends EntityRepository<CoworkingSpace, Long> {
    CoworkingSpace getCoworkingSpaceWithReservations(Long coworkingSpaceId);
}
