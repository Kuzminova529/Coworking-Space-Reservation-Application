package org.reservationapplication.model;

public enum CoworkingSpaceType {
    OPENSPACE("OPENSPACE"),
    PRIVATE("PRIVATE"),
    ROOM("ROOM");

    private String type;

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