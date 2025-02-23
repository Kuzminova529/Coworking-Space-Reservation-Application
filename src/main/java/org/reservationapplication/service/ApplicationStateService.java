package org.reservationapplication.service;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reservationapplication.model.ApplicationState;
import org.reservationapplication.model.User;

import java.io.File;
import java.io.IOException;

public class ApplicationStateService {
    private static final String APPLICATION_FILE_NAME = "application_state.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private ApplicationState applicationState;

    public ApplicationStateService(User currentUser, CoworkingSpaceService coworkingSpaceService, ReservationService reservationService) {
        this.applicationState = new ApplicationState(currentUser, coworkingSpaceService, reservationService);
    }

    public void saveState(){
        try{
            objectMapper.writeValue(new File(APPLICATION_FILE_NAME), this.applicationState );
        } catch (StreamWriteException e) {
            e.getMessage();
        } catch (DatabindException e) {
            e.getMessage();
        } catch (IOException e) {
            e.getMessage()
            ;
        }
    }
}
