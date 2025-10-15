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


    private static final String CODE_123 = "ADMIN123";
    private static final String CODE_456 = "ADMIN456";
    private static final String CODE_789 = "ADMIN789";
    private static final String CODE_999 = "ADMIN999";
    private Admin admin1;
    private Admin admin2;
    private Inventory inventory1;

    @BeforeEach
    void setUp() {
        inventory1 = new Inventory(List.of());
    admin1 = new Admin("John", "Doe", "", CODE_123, inventory1);
    admin2 = new Admin("Jane", "Smith", "", CODE_456, null);
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
        when(adminRepository.findById(eq(1L))).thenReturn(Optional.of(admin1));

        Admin admin = adminService.getById(1L);

        assertNotNull(admin);
        assertEquals("ADMIN123", admin.getAccessCode());
        verify(adminRepository, times(1)).findById(eq(1L));
    }

    @Test
    @DisplayName("getById should throw exception when admin not found")
    void testGetByIdNotFound() {
        when(adminRepository.findById(eq(999L))).thenReturn(Optional.empty());


        Exception exception = assertThrows(IllegalStateException.class, () ->
            adminService.getById(999L)
        );

        assertEquals("Admin not found with id: 999", exception.getMessage());
        verify(adminRepository, times(1)).findById(eq(999L));
    }

    //tests for updateInventory method

    @Test
    @DisplayName("update: accessCode igual, inventario igual o nulo")
    void updateAccessCodeIgualInventarioIgualONulo() {
        Inventory inv = new Inventory(List.of());
        setId(inv, 1L);
        Admin original = new Admin("John", "Doe", "", CODE_123, inv);
        Admin details = new Admin("John2", "Doe2", "", CODE_123, inv);
        setId(original, 1L);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(original));
        when(adminRepository.save(any(Admin.class))).thenAnswer(invoc -> invoc.getArgument(0));
        Admin result = adminService.update(1L, details);
        assertEquals("John2", result.getName());
        assertEquals("Doe2", result.getFirstLastName());
        assertEquals(inv, result.getAdminInventory());
    }

    @Test
    @DisplayName("update: accessCode distinto, nuevo accessCode ya existe")
    void updateAccessCodeDistintoDuplicado() {
        Inventory inv = new Inventory(List.of());
        setId(inv, 1L);
        Admin original = new Admin("John", "Doe", "", CODE_123, inv);
        Admin details = new Admin("John", "Doe", "", CODE_456, inv);
        setId(original, 1L);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(original));
        when(adminRepository.findByAccessCode(CODE_456)).thenReturn(Optional.of(new Admin("Other", "O", "", CODE_456, null)));
        Exception ex = assertThrows(IllegalStateException.class, () -> adminService.update(1L, details));
        assertTrue(ex.getMessage().contains("Access code already exists"));
    }

    @Test
    @DisplayName("update: accessCode distinto, nuevo accessCode libre")
    void updateAccessCodeDistintoLibre() {
        Inventory inv = new Inventory(List.of());
        setId(inv, 1L);
        Admin original = new Admin("John", "Doe", "", CODE_123, inv);
        Admin details = new Admin("John", "Doe", "", CODE_789, inv);
        setId(original, 1L);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(original));
        when(adminRepository.findByAccessCode(CODE_789)).thenReturn(Optional.empty());
        when(adminRepository.save(any(Admin.class))).thenAnswer(invoc -> invoc.getArgument(0));
        Admin result = adminService.update(1L, details);
        assertEquals(CODE_789, result.getAccessCode());
    }

    @Test
    @DisplayName("update: inventario nuevo, inventario no existe")
    void updateInventarioNuevoNoExiste() {
        Inventory inv = new Inventory(List.of());
        setId(inv, 1L);
        Admin original = new Admin("John", "Doe", "", CODE_123, inv);
        setId(original, 1L);
        Inventory invNuevo = new Inventory(List.of());
        setId(invNuevo, 2L);
        Admin details = new Admin("John", "Doe", "", CODE_123, invNuevo);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(original));
        when(inventoryRepository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(IllegalStateException.class, () -> adminService.update(1L, details));
        assertTrue(ex.getMessage().contains("Inventory not found"));
    }

    @Test
    @DisplayName("update: inventario nuevo, inventario ya asignado a otro admin")
    void updateInventarioNuevoYaAsignadoOtro() {
        Inventory inv = new Inventory(List.of());
        setId(inv, 1L);
        Admin original = new Admin("John", "Doe", "", CODE_123, inv);
        setId(original, 1L);
        Inventory invNuevo = new Inventory(List.of());
        setId(invNuevo, 2L);
        Admin details = new Admin("John", "Doe", "", CODE_123, invNuevo);
        Admin otroAdmin = new Admin("Other", "O", "", CODE_999, null);
        setId(otroAdmin, 99L);
        // Aseguramos que el admin retornado por el mock tenga el ID asignado
        when(adminRepository.findById(1L)).thenReturn(Optional.of(original));
        when(inventoryRepository.findById(2L)).thenReturn(Optional.of(invNuevo));
        when(adminRepository.findByAdminInventory_Id(2L)).thenAnswer(x -> {
            setId(otroAdmin, 99L);
            return Optional.of(otroAdmin);
        });
        Exception ex = assertThrows(IllegalStateException.class, () -> adminService.update(1L, details));
        assertTrue(ex.getMessage().contains("Inventory already assigned to admin id"));
    }

    @Test
    @DisplayName("update: inventario nuevo, inventario libre")
    void updateInventarioNuevoLibre() {
        Inventory inv = new Inventory(List.of());
        setId(inv, 1L);
        Admin original = new Admin("John", "Doe", "", CODE_123, inv);
        setId(original, 1L);
        Inventory invNuevo = new Inventory(List.of());
        setId(invNuevo, 2L);
        Admin details = new Admin("John", "Doe", "", CODE_123, invNuevo);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(original));
        when(inventoryRepository.findById(2L)).thenReturn(Optional.of(invNuevo));
        when(adminRepository.findByAdminInventory_Id(2L)).thenReturn(Optional.empty());
        when(adminRepository.save(any(Admin.class))).thenAnswer(invoc -> invoc.getArgument(0));
        Admin result = adminService.update(1L, details);
        assertEquals(invNuevo, result.getAdminInventory());
    }

    @Test
    @DisplayName("update: inventario en details es null")
    void updateInventarioNull() {
        Inventory inv = new Inventory(List.of());
        setId(inv, 1L);
        Admin original = new Admin("John", "Doe", "", CODE_123, inv);
        setId(original, 1L);
        Admin details = new Admin("John", "Doe", "", CODE_123, null);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(original));
        when(adminRepository.save(any(Admin.class))).thenAnswer(invoc -> invoc.getArgument(0));
        Admin result = adminService.update(1L, details);
        assertNull(result.getAdminInventory());
    }

    // Utilidad para simular IDs en Inventory y Admin
    private void setId(Object obj, Long id) {
        if (obj instanceof Admin a) {
            a.setId(id);
        } else if (obj instanceof Inventory i) {
            i.setId(id);
        } else {
            throw new IllegalArgumentException("Solo se permite Admin o Inventory");
        }
    }

    // Tests for delete method

    @Test
    @DisplayName("delete: borra admin existente")
    void deleteAdminExistente() {
        Admin admin = new Admin("John", "Doe", "", CODE_123, null);
        admin.setId(1L);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        adminService.delete(1L);

        verify(adminRepository).delete(admin);
    }

    @Test
    @DisplayName("delete: lanza excepción si admin no existe")
    void deleteAdminNoExiste() {
        when(adminRepository.findById(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalStateException.class, () -> adminService.delete(99L));
        assertEquals("Admin not found with id: 99", ex.getMessage());
        verify(adminRepository, never()).delete(any());
    }

    // Tests for create method

    @Test
    @DisplayName("create: lanza excepción si accessCode duplicado")
    void createAccessCodeDuplicado() {
        Admin admin = new Admin("John", "Doe", "", CODE_123, null);
        when(adminRepository.findByAccessCode(CODE_123)).thenReturn(Optional.of(admin));
        Exception ex = assertThrows(IllegalStateException.class, () -> adminService.create(admin));
        assertTrue(ex.getMessage().contains("Access code already exists"));
        verify(adminRepository, never()).save(any());
    }

    @Test
    @DisplayName("create: inventario nulo")
    void createInventarioNulo() {
        Admin admin = new Admin("John", "Doe", "", CODE_123, null);
        when(adminRepository.findByAccessCode(CODE_123)).thenReturn(Optional.empty());
        when(adminRepository.save(any(Admin.class))).thenAnswer(inv -> inv.getArgument(0));
        Admin result = adminService.create(admin);
        assertNull(result.getAdminInventory());
        verify(adminRepository).save(admin);
    }

    @Test
    @DisplayName("create: inventario no existe")
    void createInventarioNoExiste() {
        Inventory inv = new Inventory(List.of());
        inv.setId(2L);
        Admin admin = new Admin("John", "Doe", "", CODE_123, inv);
        when(adminRepository.findByAccessCode(CODE_123)).thenReturn(Optional.empty());
        when(inventoryRepository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(IllegalStateException.class, () -> adminService.create(admin));
        assertTrue(ex.getMessage().contains("Inventory not found"));
        verify(adminRepository, never()).save(any());
    }

    @Test
    @DisplayName("create: inventario ya asignado a otro admin")
    void createInventarioYaAsignado() {
        Inventory inv = new Inventory(List.of());
        inv.setId(2L);
        Admin admin = new Admin("John", "Doe", "", CODE_123, inv);
        Admin otroAdmin = new Admin("Other", "O", "", CODE_999, inv);
        otroAdmin.setId(99L);
        when(adminRepository.findByAccessCode(CODE_123)).thenReturn(Optional.empty());
        when(inventoryRepository.findById(2L)).thenReturn(Optional.of(inv));
        when(adminRepository.findByAdminInventory_Id(2L)).thenReturn(Optional.of(otroAdmin));
        Exception ex = assertThrows(IllegalStateException.class, () -> adminService.create(admin));
        assertTrue(ex.getMessage().contains("Inventory already assigned to admin id"));
        verify(adminRepository, never()).save(any());
    }

    @Test
    @DisplayName("create: éxito con inventario válido y libre")
    void createExitoInventarioValidoLibre() {
        Inventory inv = new Inventory(List.of());
        inv.setId(2L);
        Admin admin = new Admin("John", "Doe", "", CODE_123, inv);
        when(adminRepository.findByAccessCode(CODE_123)).thenReturn(Optional.empty());
        when(inventoryRepository.findById(2L)).thenReturn(Optional.of(inv));
        when(adminRepository.findByAdminInventory_Id(2L)).thenReturn(Optional.empty());
        when(adminRepository.save(any(Admin.class))).thenAnswer(invoc -> invoc.getArgument(0));
        Admin result = adminService.create(admin);
        assertEquals(inv, result.getAdminInventory());
        verify(adminRepository).save(admin);
    }

    // Tests for setInventory method

    @Test
    @DisplayName("setInventory: lanza excepción si admin no existe")
    void setInventoryAdminNoExiste() {
        when(adminRepository.findById(99L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(IllegalStateException.class, () -> adminService.setInventory(99L, 2L));
        assertEquals("Admin not found with id: 99", ex.getMessage());
    }

    @Test
    @DisplayName("setInventory: lanza excepción si inventario no existe")
    void setInventoryInventarioNoExiste() {
        Admin admin = new Admin("John", "Doe", "", CODE_123, null);
        admin.setId(1L);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(inventoryRepository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(IllegalStateException.class, () -> adminService.setInventory(1L, 2L));
        assertTrue(ex.getMessage().contains("Inventory not found"));
    }

    @Test
    @DisplayName("setInventory: lanza excepción si inventario ya asignado a otro admin")
    void setInventoryYaAsignadoOtroAdmin() {
        Admin admin = new Admin("John", "Doe", "", CODE_123, null);
        admin.setId(1L);
        Inventory inv = new Inventory(List.of());
        inv.setId(2L);
        Admin otroAdmin = new Admin("Other", "O", "", CODE_999, inv);
        otroAdmin.setId(99L);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(inventoryRepository.findById(2L)).thenReturn(Optional.of(inv));
        when(adminRepository.findByAdminInventory_Id(2L)).thenReturn(Optional.of(otroAdmin));
        Exception ex = assertThrows(IllegalStateException.class, () -> adminService.setInventory(1L, 2L));
        assertTrue(ex.getMessage().contains("Inventory already assigned to admin id"));
    }

    @Test
    @DisplayName("setInventory: éxito cuando inventario está libre o asignado al mismo admin")
    void setInventoryExito() {
        Admin admin = new Admin("John", "Doe", "", CODE_123, null);
        admin.setId(1L);
        Inventory inv = new Inventory(List.of());
        inv.setId(2L);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(inventoryRepository.findById(2L)).thenReturn(Optional.of(inv));
        when(adminRepository.findByAdminInventory_Id(2L)).thenReturn(Optional.empty());
        when(adminRepository.save(any(Admin.class))).thenAnswer(invoc -> invoc.getArgument(0));
        Admin result = adminService.setInventory(1L, 2L);
        assertEquals(inv, result.getAdminInventory());
        verify(adminRepository).save(admin);
    }
}
