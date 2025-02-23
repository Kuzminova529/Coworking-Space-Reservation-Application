package org.reservationapplication.exeption;

public class CoworkingSpaceNotFoundException extends RuntimeException {
    public CoworkingSpaceNotFoundException(long id) {
        super("Coworking space with ID " + id + " not found.");
    }
}
