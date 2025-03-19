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
public abstract class User {
    private static long nextId = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    public User(){
        this.id = nextId++;
    }

    public void setId(long id){
        this.id = id;
    }
    public long getId() {
        return id;
    }
}