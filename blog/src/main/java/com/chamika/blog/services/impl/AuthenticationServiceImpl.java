package com.chamika.blog.services.impl;

import com.chamika.blog.domain.entities.User;
import com.chamika.blog.repositories.UserRepository;
import com.chamika.blog.security.BlogUserDetails;
import com.chamika.blog.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor  //will inject to final fields
public class AuthenticationServiceImpl implements AuthenticationService {

    // default in spring security
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;

    private final Long jwtExpiryMs = 86400000L;

    @Override
    // validate the user is in the database
    public UserDetails authenticate(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        //if the authentication is success the return will happen
        // if the authentication is unsuccess an exception will throw
        return userDetailsService.loadUserByUsername(email);
    }

    @Override
    public UserDetails register(String name, String email, String password) {
        // 1. Check if user already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        // 2. Create new user
        User newUser = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        // 3. Save user
        User savedUser = userRepository.save(newUser);

        // 4. Return UserDetails
        return new BlogUserDetails(savedUser);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername()) // put username in to the token
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiryMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public UserDetails validateToken(String token) {
        String username = extractUserName(token);
        return userDetailsService.loadUserByUsername(username);
    }

    private String extractUserName(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    private Key getSigningKey(){
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}


//when we say "delegate", it means:
//  ✨ Passing the responsibility of a task to another component (or class) that is designed to handle it.
//You don’t do the logic yourself — you hand it off.



//✅ What is a Byte Array in simple terms?
//A byte array is a sequence (or list) of bytes.
//Each byte is an 8-bit unit of data — the smallest addressable unit in memory.
//
//In programming:
//
//A byte holds a number between 0 and 255 (or -128 to 127 if signed)
//
//A byte array holds multiple bytes, like an array of numbers.

//byte[] byteArray = { 65, 66, 67 };
