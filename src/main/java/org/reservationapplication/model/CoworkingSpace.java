package org.reservationapplication.model;

import java.util.Objects;

public class CoworkingSpace {
    private long id;
    private CoworkingSpaceType type;
    private double price;
    private AvailabilityStatus availabilityStatus;
  
    public long getID() {
        return id;
    }

    public void setID(long coworkingSpaceID) {
        this.id = coworkingSpaceID;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoworkingSpace that = (CoworkingSpace) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public String toString() {
        return "CoworkingSpace :" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", availabilityStatus=" + availabilityStatus ;
    }

}