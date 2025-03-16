package org.reservationapplication.model;

public enum AvailabilityStatus {
    AVAILABLE("AVAILABLE"),
    UNAVAILABLE("UNAVAILABLE");

    private String status;

    AvailabilityStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String toString() {
        return status;
    }
}
