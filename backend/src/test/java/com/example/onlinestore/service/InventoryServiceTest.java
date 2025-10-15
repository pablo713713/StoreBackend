package com.example.onlinestore.service;

import com.example.onlinestore.repository.InventoryRepository;
import com.example.onlinestore.model.Inventory;
import com.example.onlinestore.repository.CategoryRepository;
import com.example.onlinestore.model.Category;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    private static final Inventory INVENTORY = new Inventory(java.util.List.of());

    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        // Preparar mocks y datos comunes
    }

    //test for getAll method

    @Test
    void getAllRetornaLista() {
        java.util.List<Inventory> inventarios = java.util.List.of(INVENTORY);
        when(inventoryRepository.findAll()).thenReturn(inventarios);
        var result = inventoryService.getAll();
        assertEquals(inventarios, result);
        verify(inventoryRepository).findAll();
    }

    //test for getById method

    @Test
    void getByIdDevuelveInventarioSiExiste() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(INVENTORY));
        Inventory result = inventoryService.getById(1L);
        assertEquals(INVENTORY, result);
        verify(inventoryRepository).findById(1L);
    }

    @Test
    void getByIdLanzaExcepcionSiNoExiste() {
        when(inventoryRepository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(IllegalStateException.class, () ->
            inventoryService.getById(2L)
        );
        assertTrue(ex.getMessage().contains("Inventory not found"));
        verify(inventoryRepository).findById(2L);
    }

    //test for create method
    
    @Test
    void createGuardaInventarioConCategoriasNoNull() {
        Inventory inv = new Inventory(java.util.List.of());
        inv.setLastUpdate(null); 
        Inventory invGuardado = new Inventory(java.util.List.of());
        when(inventoryRepository.save(inv)).thenReturn(invGuardado);
        Inventory result = inventoryService.create(inv);
        assertEquals(invGuardado, result);
        verify(inventoryRepository).save(inv);
    }

    @Test
    void createGuardaInventarioConCategoriasNull() {
        Inventory inv = new Inventory(null);
        Inventory invVacio = new Inventory(java.util.List.of());
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(invVacio);
        Inventory result = inventoryService.create(inv);
        assertEquals(invVacio.getListCategories(), result.getListCategories());
        verify(inventoryRepository).save(any(Inventory.class));
    }

    //test for replaceCategories method
    
    @Test
    void replaceCategoriesReemplazaYGuarda() {
        Inventory inv = new Inventory(java.util.List.of());
        Category cat1 = new Category("Cat1", "desc1");
        Category cat2 = new Category("Cat2", "desc2");
        java.util.List<Long> ids = java.util.List.of(1L, 2L);
        java.util.List<Category> categorias = java.util.List.of(cat1, cat2);
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inv));
        when(categoryRepository.findAllById(ids)).thenReturn(categorias);
        when(inventoryRepository.save(inv)).thenReturn(inv);
        Inventory result = inventoryService.replaceCategories(1L, ids);
        assertEquals(categorias, result.getListCategories());
        verify(inventoryRepository).findById(1L);
        verify(categoryRepository).findAllById(ids);
        verify(inventoryRepository).save(inv);
    }

    @Test
    void replaceCategoriesConListaVacia() {
        Inventory inv = new Inventory(java.util.List.of(new Category("Cat1", "desc1")));
        java.util.List<Long> ids = java.util.List.of();
        java.util.List<Category> categorias = java.util.List.of();
        when(inventoryRepository.findById(2L)).thenReturn(Optional.of(inv));
        when(categoryRepository.findAllById(ids)).thenReturn(categorias);
        when(inventoryRepository.save(inv)).thenReturn(inv);
        Inventory result = inventoryService.replaceCategories(2L, ids);
        assertEquals(categorias, result.getListCategories());
        verify(inventoryRepository).findById(2L);
        verify(categoryRepository).findAllById(ids);
        verify(inventoryRepository).save(inv);
    }
}
