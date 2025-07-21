package com.chamika.blog.services;

import com.chamika.blog.domain.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
    Optional<User> getUserByEmail(String email);
}
