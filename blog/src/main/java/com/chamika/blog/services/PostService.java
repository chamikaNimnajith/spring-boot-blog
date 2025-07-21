package com.chamika.blog.services;

import com.chamika.blog.domain.CreatePostRequest;
import com.chamika.blog.domain.UpdatePostRequest;
import com.chamika.blog.domain.entities.Post;
import com.chamika.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post getPost(UUID id);
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);
    void deletePost(UUID id);
}
