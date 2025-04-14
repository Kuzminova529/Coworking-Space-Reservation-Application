package org.reservationapplication.domain.sql;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseSettings {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseSettings.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("File db.properties not found in classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error in loading db.properties", e);
        }
    }

    public static String getUrl() {
        return properties.getProperty("db.url");
    }

    public static String getUsername() {
        return properties.getProperty("db.username");
    }

    public static String getPassword() {
        return properties.getProperty("db.password");
    }

    public static String getDriver() {
        return properties.getProperty("db.driver");
    }
}