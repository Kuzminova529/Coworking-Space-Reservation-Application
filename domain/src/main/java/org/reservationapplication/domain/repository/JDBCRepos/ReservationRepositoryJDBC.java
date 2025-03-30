package org.reservationapplication.domain.repository.JDBCRepos;

import org.reservationapplication.domain.repository.ReservationRepository;
import org.reservationapplication.domain.sql.DatabaseConfigJDBC;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ReservationRepositoryJDBC implements ReservationRepository {

    protected DatabaseConfigJDBC config;

    public ReservationRepositoryJDBC(DatabaseConfigJDBC config) {
        this.config = config;
    }

    public DatabaseConfigJDBC getConfig() {
        return config;
    }
}
