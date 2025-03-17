package org.reservationapplication.sql;

import org.flywaydb.core.Flyway;

public class DatabaseMigration {
    public static void migrate() {

        // Flyway Configuration
        Flyway flyway = Flyway.configure()
                .dataSource(DatabaseSettings.getUrl(), DatabaseSettings.getUsername(), DatabaseSettings.getPassword())
                .locations("classpath:db/migration")
                .load();

        // Performing migrations
        flyway.migrate();

        System.out.println("Migration completed successfully.");
    }
}
