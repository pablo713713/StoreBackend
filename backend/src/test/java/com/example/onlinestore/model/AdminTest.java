package com.example.onlinestore.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Admin model unit tests")
class AdminTest {

    @Test
    @DisplayName("JPA no-args constructor (protected) debe existir y crear instancia")
    void jpaNoArgsConstructor_exists() {
        Admin admin = new Admin(); // mismo paquete => acceso a constructor protegido
        assertNotNull(admin);
    }

    @Test
    @DisplayName("Constructor con argumentos debe inicializar los campos (User + Admin)")
    void argsConstructor_initializesFields() {
        Inventory inventory = new Inventory(); // se asume ctor por defecto
        Admin admin = new Admin(
                "Alex",
                "Aguirre",
                "Mendoza",
                "CODE-123",
                inventory
        );

        // Campos heredados de User (se asume que existen getters)
        assertEquals("Alex", admin.getName());
        assertEquals("Aguirre", admin.getFirstLastName());
        assertEquals("Mendoza", admin.getSecondLastName());

        // Campos propios de Admin
        assertEquals("CODE-123", admin.getAccessCode());
        assertSame(inventory, admin.getAdminInventory());
    }

    @Test
    @DisplayName("get/setAccessCode debe guardar y retornar el valor asignado")
    void accessCode_getterSetter() {
        Admin admin = new Admin();
        assertNull(admin.getAccessCode(), "Por defecto accessCode debería ser null");

        admin.setAccessCode("SEC-999");
        assertEquals("SEC-999", admin.getAccessCode());

        admin.setAccessCode(null);
        assertNull(admin.getAccessCode(), "Debe aceptar null sin lanzar excepción");
    }

    @Test
    @DisplayName("get/setAdminInventory debe guardar y retornar la misma referencia")
    void adminInventory_getterSetter() {
        Admin admin = new Admin();
        assertNull(admin.getAdminInventory(), "Por defecto adminInventory debería ser null");

        Inventory inv1 = new Inventory();
        admin.setAdminInventory(inv1);
        assertSame(inv1, admin.getAdminInventory(), "Debe retornar la misma referencia asignada");

        admin.setAdminInventory(null);
        assertNull(admin.getAdminInventory(), "Debe aceptar null sin lanzar excepción");
    }
}
