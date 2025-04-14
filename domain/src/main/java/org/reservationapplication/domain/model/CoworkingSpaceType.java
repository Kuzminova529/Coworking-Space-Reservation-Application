package org.reservationapplication.domain.model;

public enum CoworkingSpaceType {
    OPENSPACE("OPENSPACE"),
    PRIVATE("PRIVATE"),
    ROOM("ROOM");

    private final String type;

    CoworkingSpaceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return type;
    }
}