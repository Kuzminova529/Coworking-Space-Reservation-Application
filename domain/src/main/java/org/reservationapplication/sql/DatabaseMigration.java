package org.reservationapplication.sql;

import org.flywaydb.core.Flyway;

public class DatabaseMigration {
    public static void migrate() {

        // Database connection settings
        String url = "jdbc:postgresql://localhost:5432/reservation_app_db";
        String user = "postgres";
        String password = "1234";

        // Flyway Configuration
        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .locations("classpath:db/migration")
                .load();

        // Performing migrations
        flyway.migrate();

        System.out.println("Migration completed successfully.");
    }
}
