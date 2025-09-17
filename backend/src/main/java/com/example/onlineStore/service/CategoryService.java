package com.example.onlineStore.service;

import com.example.onlineStore.model.Category;
import com.example.onlineStore.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Category not found with id: " + id));
    }

    public Category create(Category category) {
        categoryRepository.findByNameCategory(category.getNameCategory())
                .ifPresent(c -> {
                    throw new IllegalStateException("Category name already exists: " + c.getNameCategory());
                });
        return categoryRepository.save(category);
    }

    public Category update(Long id, Category details) {
        Category cat = getById(id);
        if (!cat.getNameCategory().equals(details.getNameCategory())) {
            categoryRepository.findByNameCategory(details.getNameCategory())
                    .ifPresent(c -> {
                        throw new IllegalStateException("Category name already exists: " + c.getNameCategory());
                    });
        }
        cat.setNameCategory(details.getNameCategory());
        cat.setDescription(details.getDescription());
        return categoryRepository.save(cat);
    }

    public void delete(Long id) {
        Category cat = getById(id);
        if (cat.getListProducts() != null && !cat.getListProducts().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with assigned products.");
        }
        categoryRepository.delete(cat);
    }
}
