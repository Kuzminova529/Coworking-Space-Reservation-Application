package org.reservationapplication.domain.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DatabaseConfigJDBC {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DatabaseSettings.getUrl());
        config.setUsername(DatabaseSettings.getUsername());
        config.setPassword(DatabaseSettings.getPassword());
        config.setDriverClassName(DatabaseSettings.getDriver());
        config.setMaximumPoolSize(10); // Maximum number of connections in the pool
        dataSource = new HikariDataSource(config);
    }

    @Bean
    public DataSource dataSource() {
        return dataSource;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
