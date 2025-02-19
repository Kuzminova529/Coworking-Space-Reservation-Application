package org.reservationapplication;

import java.util.UUID;

public class CoworkingSpace {
    private final String coworkingSpaceID = UUID.randomUUID().toString();
    private TypeOfCoworkingSpace type;
    private double price;
    private AvailabilityStatus availabilityStatus;


    public CoworkingSpace(){
        GeneralCoworkingSpaceList.addCoworkingSpace(this);
    };
    public CoworkingSpace(TypeOfCoworkingSpace type, double price, AvailabilityStatus availabilityStatus) {
        this.type = type;
        this.price = price;
        this.availabilityStatus = availabilityStatus;
        GeneralCoworkingSpaceList.addCoworkingSpace(this);
    }

    public void setType(TypeOfCoworkingSpace type) {
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

    public String getCoworkingSpaceID() {
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
