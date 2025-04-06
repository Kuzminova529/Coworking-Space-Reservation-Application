package org.reservationapplication.domain.model;

public enum UserRole {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_CUSTOMER("ROLE_ADMIN");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String toString() {
        return role;
    }
}
