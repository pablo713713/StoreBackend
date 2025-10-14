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


    @Test
    @DisplayName("getAll should return list of all admins")
    void testGetAll() {
        when(adminRepository.findAll()).thenReturn(List.of(admin1, admin2));

        List<Admin> admins = adminService.getAll();

        assertNotNull(admins);
        assertEquals(2, admins.size());
        verify(adminRepository, times(1)).findAll();
    }

}
