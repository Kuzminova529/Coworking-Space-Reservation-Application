package org.reservationapplication;

import java.util.UUID;

public abstract class User {
    private final String  userID = UUID.randomUUID().toString();
    private boolean adminRoleStatus;

    public boolean isAdminRoleStatus() {
        return adminRoleStatus;
    }
    public String  getId() {
        return userID;
    }


}
