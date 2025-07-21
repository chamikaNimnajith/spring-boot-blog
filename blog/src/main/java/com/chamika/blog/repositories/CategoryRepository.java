package com.chamika.blog.repositories;

import com.chamika.blog.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category,UUID> {


//    will return:
//    A list of Category entities (c)
//    With their posts collection eagerly loaded
//    Eager loading is a strategy where associated entities are loaded immediately
//    along with the parent entity, rather than being loaded on-demand (lazy loading
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.posts")
    List<Category> findAllWithPostCount();


    boolean existsByNameIgnoreCase(String name);
}
