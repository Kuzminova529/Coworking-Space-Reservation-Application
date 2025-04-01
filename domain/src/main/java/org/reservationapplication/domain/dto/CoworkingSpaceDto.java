package org.reservationapplication.domain.dto;

import org.reservationapplication.domain.model.CoworkingSpaceType;

public class CoworkingSpaceDto {
    private Long id;
    private CoworkingSpaceType type;
    private double price;
    private boolean active;

    public CoworkingSpaceDto() {}

    public CoworkingSpaceDto(Long id, CoworkingSpaceType type, double price, boolean active) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
