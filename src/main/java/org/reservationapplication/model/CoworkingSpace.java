package org.reservationapplication.model;

import static org.reservationapplication.repository.CoworkingSpaceRepository.getNextID;

public class CoworkingSpace {
    private long coworkingSpaceID;
    private CoworkingSpaceType type;
    private double price;
    private AvailabilityStatus availabilityStatus;

    public CoworkingSpace() {
        this.coworkingSpaceID = getNextID();//gets from CoworkingSpaceRepository
    };


    public long getID() {
        return coworkingSpaceID;
    }

    public void setID(long coworkingSpaceID) {
        this.coworkingSpaceID = coworkingSpaceID;
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

    @Override
    public String toString() {
        return "CoworkingSpace :" +
                "id='" + coworkingSpaceID + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", availabilityStatus=" + availabilityStatus ;
    }

}