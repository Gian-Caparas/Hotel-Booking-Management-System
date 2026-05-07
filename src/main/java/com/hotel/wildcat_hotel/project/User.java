package com.hotel.wildcat_hotel.project;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_name")
    private String username;

    @Column(name = "user_pass")
    private String password;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @Column(name = "role")
    private String role; // "Admin", "Staff", or "Customer"

    public User() {}

    // ✅ New constructor with String role
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.isAdmin = "Admin".equals(role);
    }

    // ✅ Old constructor kept for backward compatibility
    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.role = isAdmin ? "Admin" : "Staff";
    }

    // Getters
    public String getUsername()  { return username; }
    public String getPassword()  { return password; }
    public boolean isAdmin()     { return isAdmin; }
    public String getRole()      { return role != null ? role : (isAdmin ? "Admin" : "Staff"); }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setAdmin(boolean isAdmin)    { this.isAdmin = isAdmin; }
    public void setRole(String role)         { this.role = role; this.isAdmin = "Admin".equals(role); }

    // Authentication methods
    public static boolean isUserValid(User user) {
        List<User> users = DataBase.getUsers();
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())
                    && u.getPassword().equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUserAdmin(User user) {
        List<User> users = DataBase.getUsers();
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())
                    && u.getPassword().equals(user.getPassword())) {
                return u.isAdmin();
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{username=" + username + ", role=" + getRole() + "}";
    }
}