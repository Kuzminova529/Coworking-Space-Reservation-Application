package org.reservationapplication.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reservationapplication.exeption.CoworkingSpaceNotFoundException;
import org.reservationapplication.exeption.CoworkingStorageException;
import org.reservationapplication.logger.Loggers;
import org.reservationapplication.model.CoworkingSpace;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoworkingSpaceRepository implements EntityRepository<CoworkingSpace, Long> {
    private static final String COWORKING_FILE_NAME = "coworking_spaces.json";
    private ObjectMapper objectMapper;
    private static long nextId = 0L;

    public CoworkingSpaceRepository() {
        this.objectMapper = new ObjectMapper();
    }

    public static long getNextID(){
        return nextId;
    }

    @Override
    public void save(List<CoworkingSpace> coworkingSpaces) {
        File file = new File(COWORKING_FILE_NAME);

        try {
            objectMapper.writeValue(file, coworkingSpaces);
            Loggers.TECHNICAL_LOGGER.info("Coworking spaces have been successfully serialized to {}", file.getAbsolutePath());
        } catch (IOException e) {
            Loggers.TECHNICAL_LOGGER.error("Failed to serialize coworking spaces to file: {}", e.getMessage(), e);
            Loggers.USER_LOGGER.error("An error occurred while saving coworking spaces. Please try again later.");

            throw new CoworkingStorageException("Failed to save coworking spaces to file", e);
        }
    }

    @Override
    public List<CoworkingSpace> read() {
        File file = new File(COWORKING_FILE_NAME);
        List<CoworkingSpace> allCoworkingSpaces;

        try {
            if (file.exists() && file.length() > 0) {
                allCoworkingSpaces = objectMapper.readValue(file, new TypeReference<>(){});
                updateID(allCoworkingSpaces);
                Loggers.TECHNICAL_LOGGER.info("Coworking spaces have been successfully deserialized from {}", file.getAbsolutePath());
                return allCoworkingSpaces;
            } else {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            Loggers.TECHNICAL_LOGGER.error("Failed to deserialize coworking spaces from file: {}", e.getMessage(), e);
            Loggers.USER_LOGGER.error("An error occurred while reading coworking spaces. Please try again later.");

            throw new CoworkingStorageException("Failed to read coworking spaces from file", e);
        }
    }

    @Override
    public void add(CoworkingSpace coworkingSpace) {
        List<CoworkingSpace> coworkingSpaces = read();

        coworkingSpaces.add(coworkingSpace);
        save(coworkingSpaces);
    }

    @Override
    public void deleteByID(Long id) {
        List<CoworkingSpace> coworkingSpaces = read();
        boolean removed = coworkingSpaces.removeIf(coworkingSpace -> coworkingSpace.getID() == id);

        if (!removed) {
            throw new CoworkingSpaceNotFoundException(id);
        }
        save(coworkingSpaces);
    }

    public Optional<CoworkingSpace> getById(Long id) {
        return read().stream()
                .filter(coworkingSpace -> coworkingSpace.getID() == id)
                .findFirst();
    }

    @Override
    public void deleteAll() {
        List<CoworkingSpace> coworkingSpaces = new ArrayList<>();
        save(coworkingSpaces);
    }

    private void updateID(List<CoworkingSpace> generalCoworkingSpace){
        long maxID = generalCoworkingSpace.stream()
                .mapToLong(CoworkingSpace::getID)
                .max()
                .orElse(-1L);
                nextId = maxID + 1;
    }
}
