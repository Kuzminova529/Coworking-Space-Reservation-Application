package org.reservationapplication.model;

public enum CoworkingSpaceType {
    OPENSPACE("open space"),
    PRIVATE("private"),
    ROOM("room");

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
