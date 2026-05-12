package com.hotel.wildcat_hotel.project;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID")
    private int userID;

    @Column(name = "user_name", unique = true, nullable = false)
    private String username;

    @Column(name = "user_pass", nullable = false)
    private String password;

    // ENUM in DB: 'Admin' | 'Staff' | 'Customer'
    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_no", nullable = false)
    private String phoneNo;

    // ── Constructors ──────────────────────────────────────────────────────────

    public User() {}

    public User(String username, String password, String role,
                String email, String phoneNo) {
        this.username = username;
        this.password = password;
        this.email    = email;
        this.phoneNo  = phoneNo;
        setRole(role); // use validated setter
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public int    getUserID()   { return userID; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole()     { return role; }
    public String getEmail()    { return email; }
    public String getPhoneNo()  { return phoneNo; }

    /** Convenience helper — true when role is 'Admin' */
    public boolean isAdmin()    { return "Admin".equalsIgnoreCase(role); }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setUserID(int userID)       { this.userID   = userID; }
    public void setUsername(String username){ this.username  = username; }
    public void setPassword(String password){ this.password  = password; }
    public void setEmail(String email)      { this.email     = email; }
    public void setPhoneNo(String phoneNo)  { this.phoneNo   = phoneNo; }

    /**
     * Only 'Admin', 'Staff', or 'Customer' are accepted (mirrors DB ENUM).
     */
    public void setRole(String role) {
        if (role == null
                || (!role.equalsIgnoreCase("Admin")
                &&  !role.equalsIgnoreCase("Staff")
                &&  !role.equalsIgnoreCase("Customer"))) {
            throw new IllegalArgumentException(
                    "Invalid role: '" + role + "'. Use 'Admin', 'Staff', or 'Customer'.");
        }
        // Normalize to exact DB ENUM casing
        this.role = role.substring(0, 1).toUpperCase()
                  + role.substring(1).toLowerCase();
    }

    // ── Auth helpers ──────────────────────────────────────────────────────────

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

    // ── toString ──────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "User{id=" + userID
                + ", username='" + username + '\''
                + ", role='" + role + '\''
                + ", isAdmin=" + isAdmin() + "}";
    }
}