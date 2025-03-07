package org.reservationapplication.repository;

import org.reservationapplication.logger.Loggers;
import org.reservationapplication.model.ApplicationState;
import org.reservationapplication.model.User;
import org.reservationapplication.service.CoworkingSpaceServiceImpl;
import org.reservationapplication.service.ReservationServiceImpl;

import java.util.List;

public class ApplicationStateRepository {
    private final ApplicationStateDataStorage storage = new ApplicationStateDataStorage();
    private ApplicationState applicationState = new ApplicationState();

    public ApplicationStateRepository(){}

    public ApplicationStateRepository(User user, CoworkingSpaceServiceImpl coworkingSpaceService, ReservationServiceImpl reservationService){
        applicationState.setCoworkingSpaces(coworkingSpaceService.getAllCoworkingSpace());
        applicationState.setCurrentUser(user);
        applicationState.setReservations(reservationService.getAllReservation());
    }

    public ApplicationState readState() {
        try {
            applicationState = storage.load();
            return applicationState;
        } catch (RuntimeException e) {
            Loggers.TECHNICAL_LOGGER.error("Error reading application state: {}", e.getMessage(), e);
            return storage.defaultValue();
        }
    }

    public void saveState() {
        if (applicationState != null) {
            storage.save(applicationState);
        } else {
            Loggers.TECHNICAL_LOGGER.warn("No application state to save");
        }
    }
}
