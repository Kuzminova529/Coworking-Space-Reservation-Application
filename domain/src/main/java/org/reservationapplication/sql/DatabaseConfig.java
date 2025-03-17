package org.reservationapplication.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DatabaseSettings.getUrl());
        config.setUsername(DatabaseSettings.getUsername());
        config.setPassword(DatabaseSettings.getPassword());
        config.setMaximumPoolSize(10); // Maximum number of connections in the pool
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
