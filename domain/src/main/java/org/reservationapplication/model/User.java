package org.reservationapplication.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

/* @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "userType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Customer.class, name = "customer"),
        @JsonSubTypes.Type(value = Admin.class, name = "admin")
})
*/
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role_type", discriminatorType = DiscriminatorType.STRING)
public abstract class User extends EntityModel {
}