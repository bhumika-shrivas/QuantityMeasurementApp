package com.cg.user.service;

import com.cg.user.model.Role;
import com.cg.user.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();

    public UserService() {
        users.add(new User(1L, "admin@example.com", Role.ADMIN));
        users.add(new User(2L, "user@example.com", Role.USER));
    }

    public List<User> getAllUsers() {
        return users;
    }

    public Optional<User> getUserByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
}
