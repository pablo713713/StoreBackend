package com.example.onlinestore.service;

import com.example.onlinestore.repository.InventoryRepository;
import com.example.onlinestore.model.Inventory;
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
        org.mockito.Mockito.when(inventoryRepository.findAll()).thenReturn(inventarios);
        var result = inventoryService.getAll();
        org.junit.jupiter.api.Assertions.assertEquals(inventarios, result);
        org.mockito.Mockito.verify(inventoryRepository).findAll();
    }
}
