package org.reservationapplication.domain.exeption;

public class DatabaseException extends RuntimeException {
    private int errorCode;

    public DatabaseException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }
}
