package org.reservationapplication;

import java.util.ArrayList;
import java.util.List;

public class CoworkingSpace {
    private static long nextId = 0L;
    private final long coworkingSpaceID;
    private CoworkingSpaceType type;
    private double price;
    private AvailabilityStatus availabilityStatus;

    public CoworkingSpace(CoworkingSpaceService coworkingSpaceService) {
        this.coworkingSpaceID = nextId++;
        coworkingSpaceService.addCoworkingSpace(this);
    };
    public CoworkingSpace(CoworkingSpaceService generalCoworkingSpace, CoworkingSpaceType type, double price, AvailabilityStatus availabilityStatus) {
        this.coworkingSpaceID = nextId++;
        this.type = type;
        this.price = price;
        this.availabilityStatus = availabilityStatus;
        generalCoworkingSpace.addCoworkingSpace(this);
    }

    public void setType(CoworkingSpaceType type) {
        this.type = type;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public long getCoworkingSpaceID() {
        return coworkingSpaceID;
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
