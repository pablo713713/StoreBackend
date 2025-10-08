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



}
