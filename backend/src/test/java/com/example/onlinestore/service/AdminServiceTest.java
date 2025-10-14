package com.example.onlinestore.service;

import com.example.onlinestore.model.Admin;
import com.example.onlinestore.model.Inventory;
import com.example.onlinestore.repository.AdminRepository;
import com.example.onlinestore.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private AdminService adminService;


    private Admin admin1;
    private Admin admin2;
    private Inventory inventory1;

    @BeforeEach
    void setUp() {
        inventory1 = new Inventory(List.of());
        admin1 = new Admin("John", "Doe", "", "ADMIN123", inventory1);
        admin2 = new Admin("Jane", "Smith", "", "ADMIN456", null);
    }

    // Tests for getById method

    @Test
    @DisplayName("getAll should return list of all admins")
    void testGetAll() {
        when(adminRepository.findAll()).thenReturn(List.of(admin1, admin2));

        List<Admin> admins = adminService.getAll();

        assertNotNull(admins);
        assertEquals(2, admins.size());
        verify(adminRepository, times(1)).findAll();
    }

    //tests for getById method

    @Test
    @DisplayName("getById should return admin when found")
    void testGetByIdFound() {
        when(adminRepository.findById(eq("ADMIN123"))).thenReturn(Optional.of(admin1));

        Admin admin = adminService.getById("ADMIN123");

        assertNotNull(admin);
        assertEquals("ADMIN123", admin.getId());
        verify(adminRepository, times(1)).findById(eq("ADMIN123"));
    }

    @Test
    @DisplayName("getById should throw exception when admin not found")
    void testGetByIdNotFound() {
        when(adminRepository.findById(eq("ADMIN999"))).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            adminService.getById("ADMIN999");
        });

        assertEquals("Admin not found with id: ADMIN999", exception.getMessage());
        verify(adminRepository, times(1)).findById(eq("ADMIN999"));
    }

}
