package com.hotel.wildcat_hotel.service;

import java.util.Optional;

import com.hotel.wildcat_hotel.project.User;
import com.hotel.wildcat_hotel.repository.UserRepository;
import com.hotel.wildcat_hotel.validation.UserValidator;

public class UserService extends AbstractCrudService<User> {

    private final UserRepository repository;

    public UserService(UserRepository repository, UserValidator validator) {
        super(repository, validator);
        this.repository = repository;
    }

    public Optional<User> authenticate(String username, String password) {
        return repository.authenticate(username, password);
    }

    // ✓ NEW: Find user by username
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    // ✓ NEW: Delete user by username (used by DeleteUserController)
    public boolean deleteByUsername(String username) {
        return repository.deleteByUsername(username);
    }
}