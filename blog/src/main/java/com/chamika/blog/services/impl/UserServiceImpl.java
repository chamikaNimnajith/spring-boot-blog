package com.chamika.blog.services.impl;

import com.chamika.blog.domain.entities.User;
import com.chamika.blog.repositories.UserRepository;
import com.chamika.blog.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUserById(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found With Id" + id));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository
                .findByEmail(email);

    }
}
