package org.reservationapplication.domain.exeption;

public class DatabaseException extends RuntimeException {
    private final DatabaseErrorCode errorCode;

    public DatabaseException(String message, DatabaseErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DatabaseException(String message, Throwable cause, DatabaseErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public DatabaseErrorCode getErrorCode() {
        return errorCode;
    }
}
