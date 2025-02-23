package org.reservationapplication.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reservationapplication.exeption.CoworkingSpaceNotFoundException;
import org.reservationapplication.model.CoworkingSpace;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CoworkingSpaceRepository {
    private static final String COWORKING_FILE_NAME = "coworking_spaces.json";
    private ObjectMapper objectMapper;

    public CoworkingSpaceRepository() {
        this.objectMapper = new ObjectMapper();
    }

    public void saveCoworkingSpace(List<CoworkingSpace> generalCoworkingSpace) {
        File file = new File(COWORKING_FILE_NAME);

        try {
            objectMapper.writeValue(file, generalCoworkingSpace);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<CoworkingSpace> loadCoworkingSpace() {
        File file = new File(COWORKING_FILE_NAME);
        List<CoworkingSpace> generalCoworkingSpace;
        try {
            if (file.exists() && file.length() > 0) {
                generalCoworkingSpace = objectMapper.readValue(file, new TypeReference<>(){});
                long maxID = generalCoworkingSpace.stream()
                        .mapToLong(CoworkingSpace::getID)
                        .max()
                        .orElse(-1L);
                org.reservationapplication.model.CoworkingSpace.setNextId(maxID + 1);
                return generalCoworkingSpace;
            } else {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public void addCoworkingSpace(CoworkingSpace coworkingSpace) {
        List<CoworkingSpace> coworkingSpaces = loadCoworkingSpace();
        coworkingSpaces.add(coworkingSpace);
        saveCoworkingSpace(coworkingSpaces);
    }
    public void deleteCoworkingSpaceBuID(long id) {
        List<CoworkingSpace> coworkingSpaces = loadCoworkingSpace();
        boolean removed = coworkingSpaces.removeIf(coworkingSpace -> coworkingSpace.getID() == id);

        if (!removed) {
            throw new CoworkingSpaceNotFoundException(id);
        }
        saveCoworkingSpace(coworkingSpaces);
    }
}
