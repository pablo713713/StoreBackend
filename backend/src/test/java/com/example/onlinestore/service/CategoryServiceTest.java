package com.example.onlinestore.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.onlinestore.model.Category;
import org.junit.jupiter.api.Test;

import com.example.onlinestore.model.Category;
import com.example.onlinestore.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    private static final Category CATEGORY = new Category("1", "Electrónica");

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;

    //test for getAll method

    @Test
    void getAllRetornaLista() {
        java.util.List<Category> categorias = java.util.List.of(CATEGORY);
        when(categoryRepository.findAll()).thenReturn(categorias);
        var result = categoryService.getAll();
        assertEquals(categorias, result);
        verify(categoryRepository).findAll();
    }
}