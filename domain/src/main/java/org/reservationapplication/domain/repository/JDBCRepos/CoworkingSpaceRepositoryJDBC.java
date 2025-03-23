package org.reservationapplication.domain.repository.JDBCRepos;

import org.reservationapplication.logger.Loggers;
import org.reservationapplication.domain.exeption.CoworkingSpaceNotFoundException;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.reservationapplication.domain.model.CoworkingSpaceType;
import org.reservationapplication.domain.repository.EntityRepository;
import org.reservationapplication.domain.sql.DatabaseConfigJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CoworkingSpaceRepositoryJDBC implements EntityRepository<CoworkingSpace, Long> {

    private DatabaseConfigJDBC config;

    public CoworkingSpaceRepositoryJDBC(DatabaseConfigJDBC config) {
        this.config = config;
    }

    public CoworkingSpaceRepositoryJDBC(){
        config = new DatabaseConfigJDBC();
    }

    @Override
    public void save(List<CoworkingSpace> coworkingSpaces) {
        String sql = "INSERT INTO coworking_spaces (id, type, price, availability_status) VALUES (?, ?, ?, ?)";
        try (Connection connection = config.getConnection();
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
            Loggers.USER_LOGGER.error("Something went wrong while saving CoworkingSpaces");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
    }

    @Override
    public List<CoworkingSpace> read() {
        List<CoworkingSpace> spaces = new ArrayList<>();
        String sql = "SELECT * FROM coworking_spaces";
        try (Connection connection = config.getConnection();
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
            Loggers.USER_LOGGER.error("Something went wrong while reading CoworkingSpaces");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
        return spaces;
    }

    @Override
    public void create(CoworkingSpace coworkingSpace) {
        String sql = "INSERT INTO coworking_spaces (type, price, availability_status) VALUES ( ?, ?, ?)";
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, coworkingSpace.getType().toString());
            statement.setDouble(2, coworkingSpace.getPrice());
            statement.setBoolean(3, coworkingSpace.getActive());
            statement.executeUpdate();
        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Something went wrong while adding CoworkingSpaces");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
    }

    @Override
    public Optional<CoworkingSpace> getById(Long id) {
        String sql = "SELECT * FROM coworking_spaces WHERE id = ?";
        try (Connection connection = config.getConnection();
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
                Loggers.USER_LOGGER.error("SQL error occurred while fetching coworking space by ID: ");
                Loggers.TECHNICAL_LOGGER.error(e.getMessage());
            }

        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Database connection error");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());        }

        return Optional.empty();
    }

    @Override
    public void updateStatus(Long id) {
        String sql = "UPDATE coworking_spaces SET is_active = ? WHERE id = ?";

        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, false);
            statement.setLong(2, id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new CoworkingSpaceNotFoundException(id, 404);
            }
        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Something went wrong while updating the status of CoworkingSpace.");
            Loggers.TECHNICAL_LOGGER.error("Database error:", e);
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM coworking_spaces";
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            Loggers.USER_LOGGER.error("Database connection error");
            Loggers.TECHNICAL_LOGGER.error(e.getMessage());
        }
    }
}

