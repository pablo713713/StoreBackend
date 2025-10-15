package com.example.onlinestore.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.onlinestore.model.Category;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import com.example.onlinestore.model.Category;
import com.example.onlinestore.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    private static final Category CATEGORY = new Category("Electrónica", "desc");

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

    //test for getById method
    
    @Test
    void getByIdDevuelveCategoriaSiExiste() {
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.of(CATEGORY));
        Category result = categoryService.getById(1L);
        assertEquals(CATEGORY, result);
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getByIdLanzaExcepcionSiNoExiste() {
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Exception ex = assertThrows(IllegalStateException.class, () ->
            categoryService.getById(1L)
        );
        assertTrue(ex.getMessage().contains("Category not found"));
        verify(categoryRepository).findById(1L);
    }

    //test for create method

    @Test
    void createGuardaYCategoriaRetornada() {
        when(categoryRepository.save(CATEGORY)).thenReturn(CATEGORY);
        Category result = categoryService.create(CATEGORY);
        assertEquals(CATEGORY, result);
        verify(categoryRepository).save(CATEGORY);
    }
    
    @Test
    void createLanzaExcepcionSiCategoriaNull() {
        assertThrows(NullPointerException.class, () ->
            categoryService.create(null)
        );
        verify(categoryRepository, never()).save(any());
    }
}