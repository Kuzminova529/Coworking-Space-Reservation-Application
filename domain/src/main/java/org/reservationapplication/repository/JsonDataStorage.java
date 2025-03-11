package org.reservationapplication.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.reservationapplication.Loggers;

import java.io.File;
import java.io.IOException;

public abstract class JsonDataStorage<T> implements DataStorage<T> {
    private final ObjectMapper objectMapper;
    private final String fileName;

    protected JsonDataStorage(String fileName) {
        this.fileName = fileName;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void save(T data) {
        File file = new File(fileName);
        try {
            objectMapper.writeValue(file, data);
            Loggers.TECHNICAL_LOGGER.info("Data successfully saved to {}", file.getAbsolutePath());
        } catch (IOException e) {
            Loggers.TECHNICAL_LOGGER.error("Failed to save data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save data", e);
        }
    }

    @Override
    public T load() {
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) {
            Loggers.TECHNICAL_LOGGER.info("File {} not found or is empty, returning default value", fileName);
            return defaultValue();
        }
        try {
            T data = objectMapper.readValue(file, getTypeReference());
            Loggers.TECHNICAL_LOGGER.info("Data successfully loaded from {}", file.getAbsolutePath());
            return data;
        } catch (IOException e) {
            Loggers.TECHNICAL_LOGGER.error("Failed to load data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load data", e);
        }
    }

    protected abstract T defaultValue();

    protected abstract TypeReference<T> getTypeReference();
}
