package com.example.onlinestore.controller;

import com.example.onlinestore.model.Category;
import com.example.onlinestore.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryControllerTest {

    private CategoryService categoryService;
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        categoryService = mock(CategoryService.class);
        categoryController = new CategoryController(categoryService);
    }

    @Test
    void testGetAll() {
        Category category = new Category("Electronics", "Tech stuff");
        when(categoryService.getAll()).thenReturn(List.of(category));

        List<Category> result = categoryController.getAll();

        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getNameCategory());
        verify(categoryService).getAll();
    }

    @Test
    void testGetByIdSuccess() {
        Category category = new Category("Books", "Reading");
        when(categoryService.getById(1L)).thenReturn(category);

        ResponseEntity<Category> response = categoryController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Books", response.getBody().getNameCategory());
        verify(categoryService).getById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        when(categoryService.getById(1L)).thenThrow(new IllegalStateException("Not found"));

        ResponseEntity<Category> response = categoryController.getById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(categoryService).getById(1L);
    }
    @Test
    void testCreateCategorySuccess() {
        Category category = new Category("Furniture", "Home items");
        when(categoryService.create(category)).thenReturn(category);

        ResponseEntity<Object> response = categoryController.create(category);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(category, response.getBody());
        verify(categoryService).create(category);
    }

    @Test
    void testCreateCategoryConflict() {
        Category category = new Category("Furniture", "Home items");
        when(categoryService.create(category))
                .thenThrow(new IllegalStateException("Category name already exists: Furniture"));

        ResponseEntity<Object> response = categoryController.create(category);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Category name already exists: Furniture", response.getBody());
        verify(categoryService).create(category);
    }
    @Test
    void testUpdateCategorySuccess() {
        Category updated = new Category("Toys", "Kids products");
        when(categoryService.update(1L, updated)).thenReturn(updated);

        ResponseEntity<Object> response = categoryController.update(1L, updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
        verify(categoryService).update(1L, updated);
    }

    @Test
    void testUpdateCategoryConflictExistsMessage() {
        Category updated = new Category("Toys", "Kids products");
        when(categoryService.update(1L, updated))
                .thenThrow(new IllegalStateException("Category name already exists"));

        ResponseEntity<Object> response = categoryController.update(1L, updated);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Category name already exists", response.getBody());
        verify(categoryService).update(1L, updated);
    }

    @Test
    void testUpdateCategoryNotFound() {
        Category updated = new Category("Toys", "Kids products");
        when(categoryService.update(1L, updated))
                .thenThrow(new IllegalStateException("Category not found"));

        ResponseEntity<Object> response = categoryController.update(1L, updated);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(categoryService).update(1L, updated);
    }


}
