package org.reservationapplication.domain.repository.JDBCRepos;

import org.reservationapplication.domain.exeption.DatabaseErrorCode;
import org.reservationapplication.domain.exeption.DatabaseException;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.model.CoworkingSpaceType;
import org.reservationapplication.domain.repository.ReservationRepository;
import org.reservationapplication.logger.Loggers;
import org.reservationapplication.domain.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class ReservationRepositoryJDBC implements ReservationRepository {

    private final DataSource dataSource;

    @Autowired
    public ReservationRepositoryJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveAll(List<Reservation> reservations) {
        String sql = "INSERT INTO reservations (id, coworking_space_id, user_id, reservation_name, start_datetime, end_datetime) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Reservation reservation : reservations) {
                statement.setLong(1, reservation.getId());
                statement.setLong(2, reservation.getCoworkingSpace().getId());
                statement.setLong(3, reservation.getUserId());
                statement.setString(4, reservation.getReservationName());
                statement.setTimestamp(5, Timestamp.valueOf(reservation.getStartDateTime()));
                statement.setTimestamp(6, Timestamp.valueOf(reservation.getEndDateTime()));
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
            throw new DatabaseException("Failed to save CoworkingSpaces", e, DatabaseErrorCode.QUERY_FAILED);
        }
    }

    public List<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(resultSet.getLong("id"));
                Long coworkingSpaceId = resultSet.getLong("coworking_space_id");
                if (!resultSet.wasNull()) {
                    Optional<CoworkingSpace> coworkingSpace = getCoworkingSpaceById(coworkingSpaceId);
                    if (coworkingSpace.isPresent()) {
                        reservation.setCoworkingSpace(coworkingSpace.get());
                    }
                }
                reservation.setUserId(resultSet.getLong("user_id"));
                reservation.setReservationName(resultSet.getString("reservation_name"));
                reservation.setStartDateTime(resultSet.getTimestamp("start_datetime").toLocalDateTime());
                reservation.setEndDateTime(resultSet.getTimestamp("end_datetime").toLocalDateTime());
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
            throw new DatabaseException("Something went wrong while reading Reservations", e, DatabaseErrorCode.QUERY_FAILED);
        }
        return reservations;
    }

    public Optional<CoworkingSpace> getCoworkingSpaceById(Long id) {
        CoworkingSpace coworkingSpace = null;
        String sql = "SELECT * FROM coworking_spaces WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                coworkingSpace = new CoworkingSpace();
                coworkingSpace.setId(resultSet.getLong("id"));
                coworkingSpace.setType(CoworkingSpaceType.valueOf(resultSet.getString("type")));
                coworkingSpace.setPrice(resultSet.getDouble("price"));
                coworkingSpace.setActive(Boolean.parseBoolean(resultSet.getString("is_active")));
                return Optional.ofNullable(coworkingSpace);
            }
        } catch (SQLException e) {
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
            throw new DatabaseException("Failed to fetch CoworkingSpace", e, DatabaseErrorCode.QUERY_FAILED);
        }
        return Optional.ofNullable(coworkingSpace);
    }

    public List<Reservation> readPersonalReservations(Long userId) {
        String sql = "SELECT * FROM reservations WHERE user_id = ?";
        List<Reservation> reservations = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(resultSet.getLong("id"));
                Long coworkingSpaceId = resultSet.getLong("coworking_space_id");
                if (!resultSet.wasNull()) {
                    Optional<CoworkingSpace> coworkingSpace = getCoworkingSpaceById(coworkingSpaceId);
                    if (coworkingSpace.isPresent()) {
                        reservation.setCoworkingSpace(coworkingSpace.get());
                    }
                }
                reservation.setUserId(resultSet.getLong("user_id"));
                reservation.setReservationName(resultSet.getString("reservation_name"));
                reservation.setStartDateTime(resultSet.getTimestamp("start_datetime").toLocalDateTime());
                reservation.setEndDateTime(resultSet.getTimestamp("end_datetime").toLocalDateTime());

                reservations.add(reservation);
            }
        } catch (SQLException e) {
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
            throw new DatabaseException("Something went wrong while reading Reservations", e, DatabaseErrorCode.QUERY_FAILED);
        }
        return reservations;
    }

    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservations ( coworking_space_id, user_id, reservation_name, start_datetime, end_datetime) VALUES ( ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, reservation.getCoworkingSpace().getId());
            statement.setLong(2, reservation.getUserId());
            statement.setString(3, reservation.getReservationName());
            statement.setTimestamp(4, Timestamp.valueOf(reservation.getStartDateTime()));
            statement.setTimestamp(5, Timestamp.valueOf(reservation.getEndDateTime()));

            statement.executeUpdate();
        } catch (SQLException e) {
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
            throw new DatabaseException("Something went wrong while crating Reservation", e, DatabaseErrorCode.QUERY_FAILED);
        }
        return reservation;
    }

    public Optional<Reservation> findByIdCustom(Long id) {
        String sql = "SELECT * FROM reservations WHERE id = ? and is_active = true";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setId(resultSet.getLong("id"));
                    Long coworkingSpaceId = resultSet.getLong("coworking_space_id");
                    if (!resultSet.wasNull()) {
                        Optional<CoworkingSpace> coworkingSpace = getCoworkingSpaceById(coworkingSpaceId);
                        if (coworkingSpace.isPresent()) {
                            reservation.setCoworkingSpace(coworkingSpace.get());
                        }
                    }                    reservation.setUserId(resultSet.getLong("user_id"));
                    reservation.setReservationName(resultSet.getString("reservation_name"));
                    reservation.setStartDateTime(resultSet.getTimestamp("start_datetime").toLocalDateTime());
                    reservation.setEndDateTime(resultSet.getTimestamp("end_datetime").toLocalDateTime());

                    return Optional.ofNullable(reservation);
                }
            } catch (SQLException e) {
                Loggers.TECHNICAL_LOGGER.error(e.getMessage());
                throw new DatabaseException("SQL error occurred while fetching resrvation by ID", e, DatabaseErrorCode.QUERY_FAILED);
            }
        } catch (SQLException e) {
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
            throw new DatabaseException("Database connection error", e, DatabaseErrorCode.CONNECTION_FAILED);
        }
        return Optional.empty();
    }

    public void updateStatus(Long id) {
        String sql = "UPDATE reservations SET is_active = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, false);
            statement.setLong(2, id);

        } catch (SQLException e) {
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
            throw new DatabaseException("Failed to update reservation", e, DatabaseErrorCode.QUERY_FAILED);
        }
    }
}
