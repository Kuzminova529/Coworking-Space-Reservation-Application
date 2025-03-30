package org.reservationapplication.domain.repository.JDBCRepos;

import org.reservationapplication.domain.repository.CoworkingSpaceRepository;
import org.reservationapplication.domain.sql.DatabaseConfigJDBC;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CoworkingSpaceRepositoryJDBC implements CoworkingSpaceRepository {

    protected DatabaseConfigJDBC config;

    public CoworkingSpaceRepositoryJDBC(DatabaseConfigJDBC config) {
        this.config = config;
    }

    public DatabaseConfigJDBC getConfig() {
        return config;
    }
}
