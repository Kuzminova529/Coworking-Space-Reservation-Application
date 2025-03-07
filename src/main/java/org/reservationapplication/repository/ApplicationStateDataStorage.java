package org.reservationapplication.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.reservationapplication.model.ApplicationState;
import org.reservationapplication.model.Customer;
import org.reservationapplication.model.User;
import org.reservationapplication.service.CoworkingSpaceServiceImpl;
import org.reservationapplication.service.ReservationServiceImpl;

public class ApplicationStateDataStorage extends JsonDataStorage<ApplicationState> {
    private static final String APPLICATION_FILE_NAME = "application_state.json";

    public ApplicationStateDataStorage() {
        super(APPLICATION_FILE_NAME);
    }

    @Override
    protected ApplicationState defaultValue() {
        User defaultUser = new Customer();
        CoworkingSpaceServiceImpl defaultCoworkingSpaceService = new CoworkingSpaceServiceImpl();
        ReservationServiceImpl defaultReservationService = new ReservationServiceImpl();
        return new ApplicationState(defaultUser, defaultCoworkingSpaceService, defaultReservationService);
    }

    @Override
    protected TypeReference<ApplicationState> getTypeReference() {
        return new TypeReference<ApplicationState>() {};
    }
}
