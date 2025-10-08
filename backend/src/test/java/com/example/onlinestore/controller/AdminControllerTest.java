package com.example.onlinestore.controller;
import org.junit.jupiter.api.Test;
import com.example.onlinestore.model.Admin;
import com.example.onlinestore.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private AdminService service;

    @InjectMocks
    private AdminController controller;

    private Admin admin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        admin = new Admin();
    }

    @Test
    void testGetAll() {
        when(service.getAll()).thenReturn(List.of(admin));
        List<Admin> result = controller.getAll();
        assertEquals(1, result.size());
        verify(service).getAll();
    }

    @Test
    void testGetByIdSuccess() {
        when(service.getById(1L)).thenReturn(admin);
        ResponseEntity<Object> response = controller.getById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(admin, response.getBody());
    }

    @Test
    void testGetByIdNotFound() {
        when(service.getById(1L)).thenThrow(new IllegalStateException());
        ResponseEntity<Object> response = controller.getById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCreateSuccess() {
        when(service.create(admin)).thenReturn(admin);
        ResponseEntity<Object> response = controller.create(admin);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(admin, response.getBody());
    }

    @Test
    void testCreateBadRequest() {
        when(service.create(admin)).thenThrow(new IllegalStateException("invalid"));
        ResponseEntity<Object> response = controller.create(admin);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateSuccess() {
        when(service.update(1L, admin)).thenReturn(admin);
        ResponseEntity<Object> response = controller.update(1L, admin);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(admin, response.getBody());
    }

    @Test
    void testUpdateConflict() {
        when(service.update(1L, admin))
                .thenThrow(new IllegalStateException("already exists"));
        ResponseEntity<Object> response = controller.update(1L, admin);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(((String) response.getBody()).contains("already exists"));
    }

    @Test
    void testUpdateNotFound() {
        when(service.update(1L, admin))
                .thenThrow(new IllegalStateException("not found"));
        ResponseEntity<Object> response = controller.update(1L, admin);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSetInventorySuccess() {
        when(service.setInventory(1L, 2L)).thenReturn(admin);
        ResponseEntity<Object> response = controller.setInventory(1L, 2L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSetInventoryConflict() {
        when(service.setInventory(1L, 2L))
                .thenThrow(new IllegalStateException("already assigned"));
        ResponseEntity<Object> response = controller.setInventory(1L, 2L);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testSetInventoryBadRequest() {
        when(service.setInventory(1L, 2L))
                .thenThrow(new IllegalStateException("invalid inventory"));
        ResponseEntity<Object> response = controller.setInventory(1L, 2L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRemoveInventorySuccess() {
        when(service.removeInventory(1L)).thenReturn(admin);
        ResponseEntity<Object> response = controller.removeInventory(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRemoveInventoryNotFound() {
        when(service.removeInventory(1L)).thenThrow(new IllegalStateException());
        ResponseEntity<Object> response = controller.removeInventory(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteSuccess() {
        ResponseEntity<Object> response = controller.delete(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).delete(1L);
    }

    @Test
    void testDeleteNotFound() {
        doThrow(new IllegalStateException()).when(service).delete(1L);
        ResponseEntity<Object> response = controller.delete(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


}
