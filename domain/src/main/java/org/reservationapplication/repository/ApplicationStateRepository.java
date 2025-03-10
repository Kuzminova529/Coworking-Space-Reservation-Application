package org.reservationapplication.repository;

import org.reservationapplication.Loggers;
import org.reservationapplication.model.ApplicationState;
import org.reservationapplication.model.CoworkingSpace;
import org.reservationapplication.model.Reservation;
import org.reservationapplication.model.User;

import java.util.List;
import java.util.TreeSet;

public class ApplicationStateRepository {
    private final ApplicationStateDataStorage storage = new ApplicationStateDataStorage();
    private ApplicationState applicationState = new ApplicationState();

    public ApplicationStateRepository(){}

    public ApplicationStateRepository(User user, List<CoworkingSpace> coworkingSpaces, TreeSet<Reservation> reservations) {
        applicationState.setCurrentUser(user);
        applicationState.setCoworkingSpaces(coworkingSpaces);
        applicationState.setReservations(reservations);
    }

    public ApplicationState readState() {
        try {
            applicationState = storage.load();
            return applicationState;
        } catch (RuntimeException e) {
            Loggers.TECHNICAL_LOGGER.error("Error reading application state: {}", e.getMessage(), e);
            applicationState = storage.defaultValue();
            saveState();
            return applicationState;
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
