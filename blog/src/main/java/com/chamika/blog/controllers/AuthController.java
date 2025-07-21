package com.chamika.blog.controllers;

import com.chamika.blog.domain.dtos.*;
import com.chamika.blog.domain.entities.User;
import com.chamika.blog.services.AuthenticationService;
import com.chamika.blog.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        // authenticate means confirm that user is in the database
        UserDetails userDetails = authenticationService.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        String tokenValue = authenticationService.generateToken(userDetails);
        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expiresIn(86400)  // within 24 hours
                .build();
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @Valid @RequestBody SignupRequestDto signupRequest) {
        // Register new user
        UserDetails userDetails = authenticationService.register(
                signupRequest.getName(),
                signupRequest.getEmail(),
                signupRequest.getPassword()
        );

        // Generate token for the new user
        String tokenValue = authenticationService.generateToken(userDetails);
        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expiresIn(86400)
                .build();

        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile(
            @AuthenticationPrincipal UserDetails userDetails) {

        // Get the authenticated user's email from UserDetails
        String email = userDetails.getUsername();

        // Fetch the complete user details from your service
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Convert to DTO and return
        UserProfileResponse profileDto = new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );

        return ResponseEntity.ok(profileDto);
    }
}