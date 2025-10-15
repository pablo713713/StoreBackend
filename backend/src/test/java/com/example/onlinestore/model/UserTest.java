package com.example.onlinestore.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User model unit tests")
class UserTest {

    @Test
    @DisplayName("Constructor JPA sin argumentos debe crear instancia válida")
    void jpaConstructor_shouldCreateInstance() {
        User user = new User();
        assertNotNull(user, "El constructor vacío debería crear una instancia válida");
        assertNull(user.getId(), "El id debería ser null por defecto");
        assertNull(user.getName(), "El nombre debería ser null por defecto");
        assertNull(user.getFirstLastName(), "El primer apellido debería ser null por defecto");
        assertNull(user.getSecondLastName(), "El segundo apellido debería ser null por defecto");
    }

    @Test
    @DisplayName("Constructor con argumentos debe inicializar todos los campos")
    void argsConstructor_shouldInitializeFields() {
        User user = new User("Diego", "Rios", "Valverde");

        assertEquals("Diego", user.getName());
        assertEquals("Rios", user.getFirstLastName());
        assertEquals("Valverde", user.getSecondLastName());
        assertNull(user.getId(), "El id debería ser null hasta que lo asigne JPA");
    }

    @Test
    @DisplayName("get/setId deben asignar y retornar el valor correctamente")
    void id_getterSetter() {
        User user = new User();
        assertNull(user.getId());

        user.setId(10L);
        assertEquals(10L, user.getId());
    }

    @Test
    @DisplayName("get/setName deben asignar y retornar el valor correctamente")
    void name_getterSetter() {
        User user = new User();
        assertNull(user.getName());

        user.setName("Alex");
        assertEquals("Alex", user.getName());

        user.setName(null);
        assertNull(user.getName());
    }

    @Test
    @DisplayName("get/setFirstLastName deben asignar y retornar el valor correctamente")
    void firstLastName_getterSetter() {
        User user = new User();
        assertNull(user.getFirstLastName());

        user.setFirstLastName("Rivero");
        assertEquals("Rivero", user.getFirstLastName());

        user.setFirstLastName(null);
        assertNull(user.getFirstLastName());
    }

    @Test
    @DisplayName("get/setSecondLastName deben asignar y retornar el valor correctamente")
    void secondLastName_getterSetter() {
        User user = new User();
        assertNull(user.getSecondLastName());

        user.setSecondLastName("Aguirre");
        assertEquals("Aguirre", user.getSecondLastName());

        user.setSecondLastName(null);
        assertNull(user.getSecondLastName());
    }

    @Test
    @DisplayName("toString debe concatenar nombre y apellidos correctamente")
    void toString_shouldConcatenateFields() {
        User user = new User("Alex", "Rivero", "Aguirre");
        String expected = "Alex Rivero Aguirre";
        assertEquals(expected, user.toString());
    }
}
