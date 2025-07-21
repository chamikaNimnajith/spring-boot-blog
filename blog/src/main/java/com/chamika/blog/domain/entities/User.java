package com.chamika.blog.domain.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    //one user has many posts
    // the combination of cascade = CascadeType.ALL, orphanRemoval = true  will ensure if the user
    // will delete all the posts by the user will be deleted
    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    //Needed if you compare two User objects like user1.equals(user2).
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(createdAt, user.createdAt);
    }

    //Purpose: Returns an integer hash code — must be consistent with equals().
    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, name, createdAt);
    }


    // every time when a user is saved to the database if created at field is null give it the current time
    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
