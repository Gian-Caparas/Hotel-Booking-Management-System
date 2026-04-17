package com.hotel.wildcat_hotel.model;

public class User {
    private String username;
    private String role; // "ADMIN" or "CUSTOMER"

    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
}