package org.reservationapplication.domain.repository.JDBCRepos;

import org.reservationapplication.domain.repository.CoworkingSpaceRepository;
import org.reservationapplication.domain.sql.DatabaseConfigJDBC;

public abstract class CoworkingSpaceRepositoryJDBC implements CoworkingSpaceRepository {

    protected DatabaseConfigJDBC config;

    public CoworkingSpaceRepositoryJDBC(DatabaseConfigJDBC config) {
        this.config = config;
    }

    public CoworkingSpaceRepositoryJDBC() {
        this.config = new DatabaseConfigJDBC();
    }

    public DatabaseConfigJDBC getConfig() {
        return config;
    }
}
