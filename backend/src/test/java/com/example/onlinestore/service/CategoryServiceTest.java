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
    private static final Category CATEGORY_UPDATED = new Category("Electrodomésticos", "nueva desc");

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

    //test for update method
    
    @Test
    void updateActualizaYCategoriaRetornada() {
        Category catOriginal = new Category("Electrónica", "desc");
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.of(catOriginal));
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));
        Category result = categoryService.update(1L, CATEGORY_UPDATED);
        assertEquals(CATEGORY_UPDATED.getNameCategory(), result.getNameCategory());
        assertEquals(CATEGORY_UPDATED.getDescription(), result.getDescription());
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(catOriginal);
    }

    @Test
    void updateLanzaExcepcionSiNoExisteId() {
        when(categoryRepository.findById(2L)).thenReturn(java.util.Optional.empty());
        assertThrows(IllegalStateException.class, () ->
            categoryService.update(2L, CATEGORY_UPDATED)
        );
        verify(categoryRepository).findById(2L);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateLanzaExcepcionSiNuevoNombreYaExiste() {
        Category catOriginal = new Category("Electrónica", "desc");
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.of(catOriginal));
        when(categoryRepository.findByNameCategory(CATEGORY_UPDATED.getNameCategory())).thenReturn(java.util.Optional.of(new Category("Electrodomésticos", "otra desc")));
        assertThrows(IllegalStateException.class, () ->
            categoryService.update(1L, CATEGORY_UPDATED)
        );
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).findByNameCategory(CATEGORY_UPDATED.getNameCategory());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateNoBuscaNombreSiNoCambiaNombre() {
        Category catOriginal = new Category("Electrónica", "desc");
        Category detalles = new Category("Electrónica", "nueva desc");
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.of(catOriginal));
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));
        Category result = categoryService.update(1L, detalles);
        assertEquals(detalles.getDescription(), result.getDescription());
        verify(categoryRepository).findById(1L);
        verify(categoryRepository, never()).findByNameCategory(any());
        verify(categoryRepository).save(catOriginal);
    }

    //test for delete method

    @Test
    void deleteEliminaCategoriaSiNoTieneProductos() {
        Category cat = new Category("Electrónica", "desc");
        cat.setListProducts(null);
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.of(cat));
        categoryService.delete(1L);
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).delete(cat);
    }

    @Test
    void deleteLanzaExcepcionSiTieneProductos() {
        Category cat = new Category("Electrónica", "desc");
        com.example.onlinestore.model.Product producto = mock(com.example.onlinestore.model.Product.class);
        java.util.List<com.example.onlinestore.model.Product> productos = java.util.List.of(producto);
        cat.setListProducts(productos);
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.of(cat));
        assertThrows(IllegalStateException.class, () ->
            categoryService.delete(1L)
        );
        verify(categoryRepository).findById(1L);
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    void deleteLanzaExcepcionSiNoExisteId() {
        when(categoryRepository.findById(2L)).thenReturn(java.util.Optional.empty());
        assertThrows(IllegalStateException.class, () ->
            categoryService.delete(2L)
        );
        verify(categoryRepository).findById(2L);
        verify(categoryRepository, never()).delete(any());
    }
}