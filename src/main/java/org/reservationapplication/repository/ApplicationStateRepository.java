package org.reservationapplication.repository;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reservationapplication.logger.TechnicalLoggable;
import org.reservationapplication.logger.UserLoggable;
import org.reservationapplication.model.ApplicationState;
import org.reservationapplication.model.Customer;
import org.reservationapplication.model.User;
import org.reservationapplication.service.CoworkingSpaceServiceImpl;
import org.reservationapplication.service.ReservationServiceImpl;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

public class ApplicationStateRepository implements TechnicalLoggable, UserLoggable {
    private static final String APPLICATION_FILE_NAME = "application_state.json";
    private ObjectMapper objectMapper = new ObjectMapper();

    private ApplicationState applicationState;

    public ApplicationStateRepository() {};

    public ApplicationStateRepository(User currentUser, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService) {
        this.applicationState = new ApplicationState(currentUser, coworkingSpaceService, reservationService);
    }

    public ApplicationState readState() {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            File file = new File(APPLICATION_FILE_NAME);

            if (!file.exists()) {
                getTechnicalLogger().info("File not found: " + APPLICATION_FILE_NAME);
                return defaultApplicationState();
            }

            return objectMapper.readValue(file, ApplicationState.class);
        } catch (IOException e) {
            getTechnicalLogger().error("Exception: {}",e.getMessage(),e);
            return defaultApplicationState();
        }
    }

    private ApplicationState defaultApplicationState() {
        User defaultUser = new Customer();
        CoworkingSpaceServiceImpl defaultCoworkingSpaceService = new CoworkingSpaceServiceImpl();
        ReservationServiceImpl defaultReservationService = new ReservationServiceImpl();

        ApplicationState defaultApplicationState = new ApplicationState( defaultUser, defaultCoworkingSpaceService, defaultReservationService);

        return defaultApplicationState;
    }

    public void saveState(){
        try {
            objectMapper.registerModule( new JavaTimeModule());//new module to serialize LocalDateTime
            objectMapper.writeValue(new File(APPLICATION_FILE_NAME), this.applicationState);
        } catch (StreamWriteException e) {
            getUserLogger().error("Stream writing error.");
            getTechnicalLogger().error("Exception: {}",e.getMessage(),e);
        } catch (DatabindException e) {
            getUserLogger().error("Data binding error.");
            getTechnicalLogger().error("Exception: {}",e.getMessage(),e);
        } catch (IOException e) {
            getUserLogger().error("Input-output error.");
            getTechnicalLogger().error("Exception: {}",e.getMessage(),e);
        }
    }
}
