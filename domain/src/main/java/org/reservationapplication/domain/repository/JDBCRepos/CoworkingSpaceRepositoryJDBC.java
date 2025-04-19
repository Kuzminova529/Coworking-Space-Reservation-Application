package org.reservationapplication.domain.repository.JDBCRepos;

import org.reservationapplication.domain.exeption.DatabaseErrorCode;
import org.reservationapplication.domain.exeption.DatabaseException;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.repository.CoworkingSpaceRepository;
import org.reservationapplication.logger.Loggers;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.model.CoworkingSpaceType;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class CoworkingSpaceRepositoryJDBC implements CoworkingSpaceRepository {

    private final DataSource dataSource;

    @Autowired
    public CoworkingSpaceRepositoryJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveAll(List<CoworkingSpace> coworkingSpaces) {
        String sql = "INSERT INTO coworking_spaces (id, type, price, availability_status) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (CoworkingSpace space : coworkingSpaces) {
                statement.setLong(1, space.getId());
                statement.setString(2, space.getType().toString());
                statement.setDouble(3, space.getPrice());
                statement.setString(4, space.getActive().toString());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
            throw new DatabaseException("Something went wrong while saving CoworkingSpaces", e, DatabaseErrorCode.QUERY_FAILED);
        }
    }

    public List<CoworkingSpace> findAll() {
        List<CoworkingSpace> spaces = new ArrayList<>();
        String sql = "SELECT * FROM coworking_spaces";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                CoworkingSpace space = new CoworkingSpace();
                space.setId(resultSet.getLong("id"));
                space.setType(CoworkingSpaceType.valueOf(resultSet.getString("type")));
                space.setPrice(resultSet.getDouble("price"));
                space.setActive(Boolean.valueOf("availability_status"));
                spaces.add(space);
            }
        } catch (SQLException e) {
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
            throw new DatabaseException("Something went wrong while reading CoworkingSpaces", e, DatabaseErrorCode.QUERY_FAILED);
        }
        return spaces;
    }

    public CoworkingSpace save(CoworkingSpace coworkingSpace) {
        String sql = "INSERT INTO coworking_spaces (type, price, availability_status) VALUES ( ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, coworkingSpace.getType().toString());
            statement.setDouble(2, coworkingSpace.getPrice());
            statement.setBoolean(3, coworkingSpace.getActive());

            statement.executeUpdate();
        } catch (SQLException e) {
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
            throw new DatabaseException("Something went wrong while creating CoworkingSpaces", e, DatabaseErrorCode.QUERY_FAILED);
        }
        return coworkingSpace;
    }

    public Optional<CoworkingSpace> getByIdOptional(Long id) {
        String sql = "SELECT * FROM coworking_spaces WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    CoworkingSpace space = new CoworkingSpace();
                    space.setId(resultSet.getLong("id"));
                    space.setType(CoworkingSpaceType.valueOf(resultSet.getString("type")));
                    space.setPrice(resultSet.getDouble("price"));
                    space.setActive(Boolean.valueOf("availability_status"));

                    return Optional.of(space);
                }
            } catch (SQLException e) {
                Loggers.TECHNICAL_LOGGER.error(e.getMessage());
                throw new DatabaseException("SQL error occurred while fetching coworking space by ID", e, DatabaseErrorCode.QUERY_FAILED);
            }
        } catch (SQLException e) {
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
            throw new DatabaseException("Database connection error", e, DatabaseErrorCode.CONNECTION_FAILED);
        }
        return Optional.empty();
    }

    public void updateStatus(Long id) {
        String sql = "UPDATE coworking_spaces SET is_active = false WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0) {
                throw new DatabaseException("Coworking space with ID " + id + " not found or already inactive", DatabaseErrorCode.DATA_NOT_FOUND);
            }

        } catch (SQLException e) {
            Loggers.TECHNICAL_LOGGER.error(e.getMessage(), e);
            throw new DatabaseException("Failed to update coworking space", e, DatabaseErrorCode.QUERY_FAILED);
        }
    }

    public CoworkingSpace getCoworkingSpaceWithReservations(Long coworkingSpaceId) {
        String sql = "SELECT c.id, c.type, c.price, c.active, r.id AS reservation_id, r.start_time, r.end_time " +
                "FROM coworking_spaces c " +
                "LEFT JOIN reservations r ON c.id = r.coworking_space_id " +
                "WHERE c.id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, coworkingSpaceId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    CoworkingSpace coworkingSpace = new CoworkingSpace();
                    coworkingSpace.setId(resultSet.getLong("id"));
                    coworkingSpace.setType(CoworkingSpaceType.valueOf(resultSet.getString("type")));
                    coworkingSpace.setPrice(resultSet.getDouble("price"));
                    coworkingSpace.setActive(resultSet.getBoolean("active"));

                    List<Reservation> reservations = new ArrayList<>();
                    do {
                        Reservation reservation = new Reservation();
                        reservation.setId(resultSet.getLong("reservation_id"));
                        reservation.setStartDateTime(resultSet.getTimestamp("start_time").toLocalDateTime());
                        reservation.setEndDateTime(resultSet.getTimestamp("end_time").toLocalDateTime());
                        reservations.add(reservation);
                    } while (resultSet.next());

                    coworkingSpace.setReservations(reservations);
                    return coworkingSpace;
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            Loggers.TECHNICAL_LOGGER.error("Error retrieving coworking space with reservations", e);
            throw new DatabaseException("Failed to retrieve coworking space with reservations", e, DatabaseErrorCode.QUERY_FAILED);
        }
    }
}

