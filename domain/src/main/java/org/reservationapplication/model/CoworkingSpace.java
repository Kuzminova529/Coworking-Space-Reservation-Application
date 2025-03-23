package org.reservationapplication.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coworking_spaces")
public class CoworkingSpace extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CoworkingSpaceType type;

    private double price;

    @OneToMany(mappedBy = "coworkingSpace")
    private List<Reservation> reservations = new ArrayList<>();

    public CoworkingSpace() {}

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

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return "CoworkingSpace :" +
                "id='" + super.getId() + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price;
    }
}