package com.hotel.wildcat_hotel.util;

import com.hotel.wildcat_hotel.model.User; // Ensure you have a User model or use a String for username

public class SessionManager {
    private static SessionManager instance;
    private User currentUser; // This replaces 'username' and 'role' strings

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

    public void startSession(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() { return currentUser; }

    public void endSession() { this.currentUser = null; }
}

