package com.example.onlinestore.controller;

import com.example.onlinestore.model.Inventory;
import com.example.onlinestore.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class InventoryControllerTest {

    private InventoryService inventoryService;
    private InventoryController inventoryController;

    @BeforeEach
    void setUp() {
        inventoryService = mock(InventoryService.class);
        inventoryController = new InventoryController(inventoryService);
    }

    // ======================= GET ALL ==========================
    @Test
    void testGetAll() {
        Inventory inventory1 = new Inventory(List.of());
        Inventory inventory2 = new Inventory(List.of());
        when(inventoryService.getAll()).thenReturn(List.of(inventory1, inventory2));

        List<Inventory> result = inventoryController.getAll();

        assertEquals(2, result.size());
        verify(inventoryService).getAll();
    }

    // ======================= GET BY ID ==========================
    @Test
    void testGetByIdFound() {
        Inventory inventory = new Inventory(List.of());
        when(inventoryService.getById(1L)).thenReturn(inventory);

        ResponseEntity<Inventory> response = inventoryController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(inventoryService).getById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        when(inventoryService.getById(1L)).thenThrow(new IllegalStateException("Inventory not found with id: 1L"));

        ResponseEntity<Inventory> response = inventoryController.getById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(inventoryService).getById(1L);
    }

    // ======================= CREATE ==========================
    @Test
    void testCreateInventorySuccess() {
        Inventory inventory = new Inventory(List.of());
        when(inventoryService.create(inventory)).thenReturn(inventory);

        ResponseEntity<Object> response = inventoryController.create(inventory);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(inventory, response.getBody());
        verify(inventoryService).create(inventory);
    }

    @Test
    void testCreateInventoryBadRequest() {
        Inventory inventory = new Inventory(List.of());
        when(inventoryService.create(inventory)).thenThrow(new IllegalStateException("Invalid inventory"));

        ResponseEntity<Object> response = inventoryController.create(inventory);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid inventory", response.getBody());
        verify(inventoryService).create(inventory);
    }

    // ======================= REPLACE CATEGORIES ==========================
    @Test
    void testReplaceCategories() {
        List<Long> categoryIds = List.of(1L, 2L);
        Inventory inventory = new Inventory(List.of());
        when(inventoryService.replaceCategories(1L, categoryIds)).thenReturn(inventory);

        ResponseEntity<Object> response = inventoryController.replaceCategories(1L, categoryIds);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inventory, response.getBody());
        verify(inventoryService).replaceCategories(1L, categoryIds);
    }

    @Test
    void testReplaceCategoriesBadRequest() {
        List<Long> categoryIds = List.of(1L, 2L);
        when(inventoryService.replaceCategories(1L, categoryIds)).thenThrow(new IllegalStateException("Invalid categories"));

        ResponseEntity<Object> response = inventoryController.replaceCategories(1L, categoryIds);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid categories", response.getBody());
        verify(inventoryService).replaceCategories(1L, categoryIds);
    }

    // ======================= ADD CATEGORY ==========================
    @Test
    void testAddCategory() {
        Inventory inventory = new Inventory(List.of());
        when(inventoryService.addCategory(1L, 1L)).thenReturn(inventory);

        ResponseEntity<Object> response = inventoryController.addCategory(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inventory, response.getBody());
        verify(inventoryService).addCategory(1L, 1L);
    }

    @Test
    void testAddCategoryBadRequest() {
        when(inventoryService.addCategory(1L, 1L)).thenThrow(new IllegalStateException("Category not found"));

        ResponseEntity<Object> response = inventoryController.addCategory(1L, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Category not found", response.getBody());
        verify(inventoryService).addCategory(1L, 1L);
    }

    // ======================= REMOVE CATEGORY ==========================
    @Test
    void testRemoveCategory() {
        Inventory inventory = new Inventory(List.of());
        when(inventoryService.removeCategory(1L, 1L)).thenReturn(inventory);

        ResponseEntity<Object> response = inventoryController.removeCategory(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inventory, response.getBody());
        verify(inventoryService).removeCategory(1L, 1L);
    }

    @Test
    void testRemoveCategoryBadRequest() {
        when(inventoryService.removeCategory(1L, 1L)).thenThrow(new IllegalStateException("Category not found"));

        ResponseEntity<Object> response = inventoryController.removeCategory(1L, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Category not found", response.getBody());
        verify(inventoryService).removeCategory(1L, 1L);
    }

    // ======================= DELETE ==========================
    @Test
    void testDeleteInventory() {
        doNothing().when(inventoryService).delete(1L);

        ResponseEntity<Object> response = inventoryController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(inventoryService).delete(1L);
    }

    @Test
    void testDeleteInventoryNotFound() {
        doThrow(new IllegalStateException("Inventory not found")).when(inventoryService).delete(1L);

        ResponseEntity<Object> response = inventoryController.delete(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(inventoryService).delete(1L);
    }
}
