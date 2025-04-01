package org.reservationapplication.domain.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CUSTOMER")
@JsonTypeName("CUSTOMER")
public class Customer extends User {
    public Customer() {
        super();
    }
}
