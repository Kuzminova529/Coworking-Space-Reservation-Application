package org.reservationapplication.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "userType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Customer.class, name = "customer"),
        @JsonSubTypes.Type(value = Admin.class, name = "admin")
})

public abstract class User {
    private static long nextId = 0L;

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