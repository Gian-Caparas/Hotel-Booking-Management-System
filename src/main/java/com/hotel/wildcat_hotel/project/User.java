package com.hotel.wildcat_hotel.project;

import com.hotel.wildcat_hotel.core.BaseEntity;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
@AttributeOverride(name = "entityId", column = @Column(name = "userID"))
public class User extends BaseEntity {

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

    public int    getUserID()   { return getEntityId(); }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole()     { return role; }
    public String getEmail()    { return email; }
    public String getPhoneNo()  { return phoneNo; }

    /** Convenience helper — true when role is 'Admin' */
    public boolean isAdmin()    { return "Admin".equalsIgnoreCase(role); }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setUserID(int userID)       { setEntityId(userID); }
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

    // ── toString ──────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "User{id=" + getUserID()
                + ", username='" + username + '\''
                + ", role='" + role + '\''
                + ", isAdmin=" + isAdmin() + "}";
    }
}