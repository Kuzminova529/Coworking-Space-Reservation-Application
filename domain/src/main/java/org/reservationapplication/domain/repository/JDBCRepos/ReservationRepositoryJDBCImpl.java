package org.reservationapplication.domain.repository.JDBCRepos;

import org.reservationapplication.logger.Loggers;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.sql.DatabaseConfigJDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository("jdbcReservationRepository")
public class ReservationRepositoryJDBCImpl extends ReservationRepositoryJDBC {

    @Autowired
    public ReservationRepositoryJDBCImpl(DatabaseConfigJDBC config) {
        super(config);
    }

    public ReservationRepositoryJDBCImpl() {
        super();
    }

    @Override
    public void save(List<Reservation> reservations) {
        String sql = "INSERT INTO reservations (id, coworking_space_id, user_id, reservation_name, start_datetime, end_datetime) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Reservation reservation : reservations) {
                statement.setLong(1, reservation.getId());
                statement.setLong(2, reservation.getCoworkingSpace().getId());
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

    @Override
    public List<Reservation> read() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(resultSet.getLong("id"));
                reservation.getCoworkingSpace().setId(resultSet.getLong("coworking_space_id"));
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

    @Override
    public List<Reservation> readPersonalReservations(Long userId) {
        String sql = "SELECT * FROM reservations WHERE user_id = ?";
        List<Reservation> reservations = new ArrayList<>();

        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(resultSet.getLong("id"));
                reservation.getCoworkingSpace().setId(resultSet.getLong("coworking_space_id"));
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

    @Override
    public void create(Reservation reservation) {
        String sql = "INSERT INTO reservations ( coworking_space_id, user_id, reservation_name, start_datetime, end_datetime) VALUES ( ?, ?, ?, ?, ?)";
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, reservation.getCoworkingSpace().getId());
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

    @Override
    public Optional<Reservation> getById(Long id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";

        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            Reservation reservation = new Reservation();
            reservation.setId(resultSet.getLong("id"));
            reservation.getCoworkingSpace().setId(resultSet.getLong("coworking_space_id"));
            reservation.setUserID(resultSet.getLong("user_id"));
            reservation.setReservationName(resultSet.getString("reservation_name"));
            reservation.setStartDateTime(resultSet.getTimestamp("start_datetime").toLocalDateTime());
            reservation.setEndDateTime(resultSet.getTimestamp("end_datetime").toLocalDateTime());

            return Optional.ofNullable(reservation);
        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Something went wrong while finding reservation");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void updateStatus(Long id) {
        String sql = "UPDATE reservations SET is_active = ? WHERE id = ?";
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, false);
            statement.setLong(2, id);

        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Something went wrong while updating the status of reservation");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
    }

}
