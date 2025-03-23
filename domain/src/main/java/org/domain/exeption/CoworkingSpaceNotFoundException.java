package org.domain.exeption;

public class CoworkingSpaceNotFoundException extends RuntimeException {
    private int errorCode;

    public CoworkingSpaceNotFoundException(long id, int errorCode) {
        super("Coworking space with ID " + id + " not found.");
        this.errorCode = errorCode;
    }
}
