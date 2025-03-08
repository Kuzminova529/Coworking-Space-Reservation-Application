package org.reservationapplication.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.reservationapplication.model.CoworkingSpace;

import java.util.ArrayList;
import java.util.List;

public class WorkspaceDataStorage extends JsonDataStorage<List<CoworkingSpace>> {
    private static final String FILE_NAME = "coworking_spaces.json";

    public WorkspaceDataStorage() {
        super(FILE_NAME);
    }

    @Override
    protected List<CoworkingSpace> defaultValue() {
        return new ArrayList<>();
    }

    @Override
    protected TypeReference<List<CoworkingSpace>> getTypeReference() {
        return new TypeReference<>() {};
    }
}
