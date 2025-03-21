package org.reservationapplication.repository.JDBCRepos;

import org.reservationapplication.Loggers;
import org.reservationapplication.exeption.CoworkingSpaceNotFoundException;
import org.reservationapplication.model.Reservation;
import org.reservationapplication.sql.DatabaseConfig;

import java.sql.*;
import java.util.Comparator;
import java.util.TreeSet;

public class ReservationRepository {
    Comparator<Reservation> dateTimeComparator = Comparator.comparing(Reservation::getStartDateTime);
    DatabaseConfig config;

    public ReservationRepository(DatabaseConfig config) {
        this.config = config;
    }

    public ReservationRepository(){
        config = new DatabaseConfig();
    }

    public void save(TreeSet<Reservation> reservations) {
        String sql = "INSERT INTO reservations (id, coworking_space_id, user_id, reservation_name, start_datetime, end_datetime) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Reservation reservation : reservations) {
                statement.setLong(1, reservation.getID());
                statement.setLong(2, reservation.getCoworkingSpace().getID());
                statement.setLong(3, reservation.getUserID());
                statement.setString(4, reservation.getReservationName());
                statement.setTimestamp(5, Timestamp.valueOf(reservation.getStartDateTime()));
                statement.setTimestamp(6, Timestamp.valueOf(reservation.getEndDateTime()));
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Something went wrong while saving reservations");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
    }

    public TreeSet<Reservation> read() {
        TreeSet<Reservation> reservations = new TreeSet<>(dateTimeComparator);
        String sql = "SELECT * FROM reservations";
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                reservation.setID(resultSet.getLong("id"));
                reservation.getCoworkingSpace().setID(resultSet.getLong("coworking_space_id"));
                reservation.setUserID(resultSet.getLong("user_id"));
                reservation.setReservationName(resultSet.getString("reservation_name"));
                reservation.setStartDateTime(resultSet.getTimestamp("start_datetime").toLocalDateTime());
                reservation.setEndDateTime(resultSet.getTimestamp("end_datetime").toLocalDateTime());
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Something went wrong while reading Reservations");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
        return reservations;
    }

    public TreeSet<Reservation> readPersonalReservations(Long userId) {
        String sql = "SELECT * FROM reservations WHERE user_id = ?";
        TreeSet<Reservation> reservations = new TreeSet<>(dateTimeComparator);

        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                reservation.setID(resultSet.getLong("id"));
                reservation.getCoworkingSpace().setID(resultSet.getLong("coworking_space_id"));
                reservation.setUserID(resultSet.getLong("user_id"));
                reservation.setReservationName(resultSet.getString("reservation_name"));
                reservation.setStartDateTime(resultSet.getTimestamp("start_datetime").toLocalDateTime());
                reservation.setEndDateTime(resultSet.getTimestamp("end_datetime").toLocalDateTime());

                reservations.add(reservation);
            }
        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Something went wrong while finding reservation");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
        return reservations;
    }


    public void create(Reservation reservation) {
        String sql = "INSERT INTO reservations ( coworking_space_id, user_id, reservation_name, start_datetime, end_datetime) VALUES ( ?, ?, ?, ?, ?)";
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, reservation.getCoworkingSpace().getID());
            statement.setLong(2, reservation.getUserID());
            statement.setString(3, reservation.getReservationName());
            statement.setTimestamp(4, Timestamp.valueOf(reservation.getStartDateTime()));
            statement.setTimestamp(5, Timestamp.valueOf(reservation.getEndDateTime()));

            statement.executeUpdate();
        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Something went wrong while adding reservation");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
    }

    public void makeInactive(Long id) {
        String sql = "UPDATE reservations SET is_active = ? WHERE id = ?";
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, false);
            statement.setLong(2, id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new CoworkingSpaceNotFoundException(id, 404);
            }
        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Something went wrong while updating the status of reservation");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM reservations";
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Database connection error");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
    }
}
