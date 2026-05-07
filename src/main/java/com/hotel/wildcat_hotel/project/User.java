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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;

    @Column(name = "user_name", unique = true)
    private String username;

    @Column(name = "user_pass")
    private String password;

    @Column(name = "is_admin")
    private boolean isAdmin;

    public User() {}

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    // Getters
    public String getUsername()  { return username; }
    public String getPassword()  { return password; }
    public boolean isAdmin()     { return isAdmin; }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setAdmin(boolean isAdmin)    { this.isAdmin = isAdmin; }

    // Authentication methods — fixed: Database → DataBase
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
        return "User{username=" + username + ", isAdmin=" + isAdmin + "}";
    }
}