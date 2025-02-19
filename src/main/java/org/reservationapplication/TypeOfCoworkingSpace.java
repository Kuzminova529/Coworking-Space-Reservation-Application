package org.reservationapplication;

public enum TypeOfCoworkingSpace {
    OPENSPACE("open space"),
    PRIVATE("private"),
    ROOM("room");

    private String type;

    TypeOfCoworkingSpace(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return type;
    }
}
