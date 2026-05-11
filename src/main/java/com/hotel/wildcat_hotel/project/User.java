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
    @Column(name = "user_name", unique = true, nullable = false)
    private String username;

    @Column(name = "user_pass")
    private String password;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @Column(name = "role")
    private String role;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    private String status;

    // Add getters for these new fields so TableView can access them
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getStatus() { return status; }
    
    
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setStatus(String status) { this.status = status; }

    public User() {}

    // CLEANED CONSTRUCTOR
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.status = "Active";
    }

    // ================= GETTERS =================
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isAdmin() { return isAdmin; }
    public String getRole() { return role; }

    // ================= SETTERS =================
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) {
        if (role == null) {
            this.role = null;
        } else {
            this.role = "Customer";
        }
        // Automatically determine admin status
        this.isAdmin = this.role != null && this.role.equalsIgnoreCase("Admin");
    }

    // ================= AUTH METHODS =================
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
        return "User{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}