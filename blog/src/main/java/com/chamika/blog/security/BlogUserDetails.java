package com.chamika.blog.security;

import com.chamika.blog.domain.entities.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Getter
@RequiredArgsConstructor
//UserDetails is a spring security default interface
public class BlogUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // every single user will have the same access rules
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UUID getId() {
        return user.getId();
    }
}



//@RequiredArgsConstructor comes from Lombok
//It auto-generates a constructor with final fields injected.


//In your class:
//Lombok will generate this constructor at compile time:
// public BlogUserDetails(User user) {
//    this.user = user;
//}


//
//@RequiredArgsConstructor	Generates constructor only for final or @NonNull fields
//@AllArgsConstructor	Generates constructor for all fields (final & non-final)