package org.reservationapplication.model;


public class CoworkingSpace {
    private static long nextId = 0L;
    private long coworkingSpaceID;
    private CoworkingSpaceType type;
    private double price;
    private AvailabilityStatus availabilityStatus;

    public CoworkingSpace() {
        this.coworkingSpaceID = nextId++;
    };

    public long getID() {
        return coworkingSpaceID;
    }

    public CoworkingSpaceType getType() {
        return type;
    }

    public void setType(CoworkingSpaceType type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public static long getNextId() {
        return nextId;
    }
    public static void setNextId(long nextId) {
        CoworkingSpace.nextId = nextId;
    }

    @Override
    public String toString() {
        return "CoworkingSpace :" +
                "id='" + coworkingSpaceID + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", availabilityStatus=" + availabilityStatus ;
    }

}