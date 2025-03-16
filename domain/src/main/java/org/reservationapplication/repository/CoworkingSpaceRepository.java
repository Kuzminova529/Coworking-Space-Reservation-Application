package org.reservationapplication.repository;

import org.reservationapplication.Loggers;
import org.reservationapplication.exeption.CoworkingSpaceNotFoundException;
import org.reservationapplication.model.AvailabilityStatus;
import org.reservationapplication.model.CoworkingSpace;
import org.reservationapplication.model.CoworkingSpaceType;
import org.reservationapplication.sql.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CoworkingSpaceRepository implements EntityRepository<CoworkingSpace, Long> {
    private static long nextId = 0L;

    public static long getNextID() {
        return nextId;
    }

    @Override
    public void save(List<CoworkingSpace> coworkingSpaces) {
        String sql = "INSERT INTO coworking_spaces (id, type, price, availability_status) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (CoworkingSpace space : coworkingSpaces) {
                statement.setLong(1, space.getID());
                statement.setString(2, space.getType().toString());
                statement.setDouble(3, space.getPrice());
                statement.setString(4, space.getAvailabilityStatus().toString());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Something went wrong while saving CoworkingSpaces");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
        updateID(coworkingSpaces);
    }

    @Override
    public List<CoworkingSpace> read() {
        List<CoworkingSpace> spaces = new ArrayList<>();
        String sql = "SELECT * FROM coworking_spaces";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                CoworkingSpace space = new CoworkingSpace();
                space.setID(resultSet.getLong("id"));
                space.setType(CoworkingSpaceType.valueOf(resultSet.getString("type")));
                space.setPrice(resultSet.getDouble("price"));
                space.setAvailabilityStatus(AvailabilityStatus.valueOf(resultSet.getString("availability_status")));
                spaces.add(space);
            }
        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Something went wrong while reading CoworkingSpaces");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
        return spaces;
    }

    @Override
    public void add(CoworkingSpace coworkingSpace) {
        String sql = "INSERT INTO coworking_spaces (id, type, price, availability_status) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, coworkingSpace.getID());
            statement.setString(2, coworkingSpace.getType().toString());
            statement.setDouble(3, coworkingSpace.getPrice());
            statement.setString(4, coworkingSpace.getAvailabilityStatus().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Something went wrong while adding CoworkingSpaces");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void deleteByID(Long id) {
        String sql = "DELETE FROM coworking_spaces WHERE id = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new CoworkingSpaceNotFoundException(id);
            }
        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Something went wrong while deleting CoworkingSpaces");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
    }

    public Optional<CoworkingSpace> getById(Long id) {
        String sql = "SELECT * FROM coworking_spaces WHERE id = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    CoworkingSpace space = new CoworkingSpace();
                    space.setID(resultSet.getLong("id"));
                    space.setType(CoworkingSpaceType.valueOf(resultSet.getString("type")));
                    space.setPrice(resultSet.getDouble("price"));
                    space.setAvailabilityStatus(AvailabilityStatus.valueOf(resultSet.getString("availability_status")));

                    return Optional.of(space);
                }
            } catch (SQLException e) {
                Loggers.USER_LOGGER.error("SQL error occurred while fetching coworking space by ID: ");
                Loggers.TECHNICAL_LOGGER.error(e.getMessage());
            }

        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Database connection error");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());        }

        return Optional.empty();
    }

    public void deleteAll() {
        String sql = "DELETE FROM coworking_spaces";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Database connection error");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
    }

    private void updateID(List<CoworkingSpace> spaces) {
        nextId = spaces.stream().mapToLong(CoworkingSpace::getID).max().orElse(0L) + 1;
    }
}

