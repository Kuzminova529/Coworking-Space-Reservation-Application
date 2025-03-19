package org.reservationapplication.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {
    public Customer() {
        super();
    }
}
