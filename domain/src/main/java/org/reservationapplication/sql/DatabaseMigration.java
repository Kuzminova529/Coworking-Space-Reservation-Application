package org.reservationapplication.sql;

import org.flywaydb.core.Flyway;
import org.reservationapplication.Loggers;

public class DatabaseMigration {
    public static void migrate() {

        // Flyway Configuration
        Flyway flyway = Flyway.configure()
                .dataSource(DatabaseSettings.getUrl(), DatabaseSettings.getUsername(), DatabaseSettings.getPassword())
                .locations("classpath:db/migration")
                .load();

        // Performing migrations
        flyway.migrate();

        Loggers.USER_LOGGER.info("Migration completed successfully.");
    }
}
