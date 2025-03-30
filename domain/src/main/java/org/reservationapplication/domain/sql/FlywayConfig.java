package org.reservationapplication.domain.sql;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    public Flyway flyway() {
        Flyway flyway = Flyway.configure()
                .dataSource(DatabaseSettings.getUrl(), DatabaseSettings.getUsername(), DatabaseSettings.getPassword())
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
        return flyway;
    }
}
