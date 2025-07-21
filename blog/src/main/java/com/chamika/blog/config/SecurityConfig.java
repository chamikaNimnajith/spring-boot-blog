package com.chamika.blog.config;

import com.chamika.blog.domain.entities.User;
import com.chamika.blog.repositories.UserRepository;
import com.chamika.blog.security.BlogUserDetailsService;
import com.chamika.blog.security.JwtAuthenticationFilter;
import com.chamika.blog.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationService authenticationService){
        return new JwtAuthenticationFilter(authenticationService);
    }

    @Bean
    // Validate users in protected endpoints (via JwtAuthenticationFilter).
    //This is a key interface in Spring Security used to load user information during authentication
    public UserDetailsService userDetailsService(UserRepository userRepository){
        BlogUserDetailsService blogUserDetailsService =  new BlogUserDetailsService(userRepository);

        //Ensures that this specific test/admin/demo user exists in DB before the app starts
        String email = "chamikanimnajith@gmail.com";
        userRepository.findByEmail(email).orElseGet(() ->{
            User newUser = User.builder()
                    .name("chamika nimnajith")
                    .email(email)
                    .password(passwordEncoder().encode("123456"))
                    .build();
            return userRepository.save(newUser);
        });

        return blogUserDetailsService;   // this object has the function loadUserByUsername
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception{
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/auth/signup").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/posts/drafts").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/v1/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/tags/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf ->csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    //This is needed when you want to perform manual authentication in your code
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}


//Stateless means:
//The server does not store any session data between requests.

//In a stateless system like a REST API:
//
//Each request must carry all the information the server needs to handle it.
//
//The server does not remember you from a previous request.
//
//Commonly used with JWT (JSON Web Tokens), API keys, or OAuth tokens.


//Example:
//You send a GET /api/user with Authorization: Bearer <token>
//
//The server validates the token and responds.
//
//        Next time, you must send the token again — server doesn’t "remember" your login.