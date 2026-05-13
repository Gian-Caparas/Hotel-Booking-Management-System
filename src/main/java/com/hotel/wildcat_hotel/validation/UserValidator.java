package com.hotel.wildcat_hotel.validation;

import com.hotel.wildcat_hotel.core.Validator;
import com.hotel.wildcat_hotel.project.User;

public class UserValidator implements Validator<User> {

    @Override
    public void validate(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null.");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username must not be blank.");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password must not be blank.");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email must not be blank.");
        }
        if (user.getPhoneNo() == null || user.getPhoneNo().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number must not be blank.");
        }
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Role must not be blank.");
        }
    }
}