package org.reservationapplication.model;

public enum AvailabilityStatus {
    AVAILABLE("available"),
    UNAVAILABLE("unavailable");

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
