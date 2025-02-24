package org.reservationapplication.model;

public abstract class User {
    private static long nextId = 0L;

    private final long  userID ;

    public User(){
        this.userID = nextId++;
    }
    
    public long  getId() {
        return userID;
    }
}
