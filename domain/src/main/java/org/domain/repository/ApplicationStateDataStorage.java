package org.domain.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.domain.model.ApplicationState;
import org.domain.model.Customer;
import org.domain.model.User;

import java.util.ArrayList;
import java.util.TreeSet;

public class ApplicationStateDataStorage extends JsonDataStorage<ApplicationState> {
    private static final String APPLICATION_FILE_NAME = "application_state.json";

    public ApplicationStateDataStorage() {
        super(APPLICATION_FILE_NAME);
    }

    @Override
    protected ApplicationState defaultValue() {
        User defaultUser = new Customer();
        return new ApplicationState(defaultUser, new ArrayList<>(), new TreeSet<>());
    }

    @Override
    protected TypeReference<ApplicationState> getTypeReference() {
        return new TypeReference<ApplicationState>() {};
    }
}
