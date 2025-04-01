package org.reservationapplication.domain.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
@JsonTypeName("ADMIN")
public class Admin extends User {
    public Admin() {
        super();
    }
}