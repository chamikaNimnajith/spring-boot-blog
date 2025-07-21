package com.chamika.blog.services.impl;


import com.chamika.blog.domain.entities.Category;
import com.chamika.blog.repositories.CategoryRepository;
import com.chamika.blog.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        if(categoryRepository.existsByNameIgnoreCase(category.getName())){
            throw new IllegalArgumentException("Category already exist with name" + category.getName());
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(UUID id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent()){
            if(!category.get().getPosts().isEmpty()){
                throw new IllegalStateException("Category has posts associated with it");
            }
            categoryRepository.deleteById(id);
        }

    }

    @Override
    public Category getCategoryByID(UUID id) {
        // we are not going to return optional category,
        // we are going to throw entity not found exception if required
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category Not Found With id" + id));

    }
}
