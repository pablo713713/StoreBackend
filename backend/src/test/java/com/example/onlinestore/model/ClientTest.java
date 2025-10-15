package com.example.onlinestore.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Client model unit tests")
class ClientTest {

    /** Subclase mínima para usar como Payment en las pruebas. */
    static class DummyPayment extends Payment {
        protected DummyPayment() { super(); }
        @Override
        protected IPaymentMethod createPaymentMethod() { return null; }
    }

    @Test
    @DisplayName("Constructor JPA sin argumentos: instancia válida y campos en null")
    void jpaConstructor_shouldCreateInstance() {
        Client c = new Client(); // mismo paquete => acceso permitido
        assertNotNull(c);

        // Heredados de User
        assertNull(c.getId());
        assertNull(c.getName());
        assertNull(c.getFirstLastName());
        assertNull(c.getSecondLastName());

        // Propios de Client
        assertNull(c.getIdClient());
        assertNull(c.getEmail());
        assertNull(c.getPassword());
        assertNull(c.getCart());
        assertNull(c.getPaymentMethod());
    }

    @Test
    @DisplayName("Constructor con argumentos debe inicializar campos heredados y propios")
    void argsConstructor_shouldInitializeFields() {
        Client c = new Client(
                "Alex",
                "Rivero",
                "Aguirre",
                "CLI-001",
                "alex@example.com",
                "secret"
        );

        // Heredados
        assertEquals("Alex", c.getName());
        assertEquals("Rivero", c.getFirstLastName());
        assertEquals("Aguirre", c.getSecondLastName());
        assertNull(c.getId(), "id debe ser null hasta persistencia");

        // Propios
        assertEquals("CLI-001", c.getIdClient());
        assertEquals("alex@example.com", c.getEmail());
        assertEquals("secret", c.getPassword());

        // Asociaciones no inicializadas
        assertNull(c.getCart());
        assertNull(c.getPaymentMethod());
    }

    @Test
    @DisplayName("get/setIdClient deben asignar y devolver el valor")
    void idClient_getterSetter() {
        Client c = new Client();
        assertNull(c.getIdClient());

        c.setIdClient("UCB-2025");
        assertEquals("UCB-2025", c.getIdClient());

        c.setIdClient(null);
        assertNull(c.getIdClient());
    }

    @Test
    @DisplayName("get/setEmail deben asignar y devolver el valor")
    void email_getterSetter() {
        Client c = new Client();
        assertNull(c.getEmail());

        c.setEmail("user@store.com");
        assertEquals("user@store.com", c.getEmail());

        c.setEmail(null);
        assertNull(c.getEmail());
    }

    @Test
    @DisplayName("get/setPassword deben asignar y devolver el valor")
    void password_getterSetter() {
        Client c = new Client();
        assertNull(c.getPassword());

        c.setPassword("p@ss");
        assertEquals("p@ss", c.getPassword());

        c.setPassword(null);
        assertNull(c.getPassword());
    }

    @Test
    @DisplayName("get/setCart deben asignar y devolver la misma referencia")
    void cart_getterSetter() {
        Client c = new Client();
        assertNull(c.getCart());

        ShoppingCart cart = Mockito.mock(ShoppingCart.class);
        c.setCart(cart);
        assertSame(cart, c.getCart());

        c.setCart(null);
        assertNull(c.getCart());
    }

    @Test
    @DisplayName("get/setPaymentMethod deben asignar y devolver la misma referencia")
    void paymentMethod_getterSetter() {
        Client c = new Client();
        assertNull(c.getPaymentMethod());

        Payment pm = new DummyPayment();
        c.setPaymentMethod(pm);
        assertSame(pm, c.getPaymentMethod());

        c.setPaymentMethod(null);
        assertNull(c.getPaymentMethod());
    }

    @Test
    @DisplayName("setId heredado (solo test) debe permitir establecer el id")
    void inherited_setId_shouldWork() {
        Client c = new Client();
        assertNull(c.getId());
        c.setId(123L); // método heredado de User para uso en tests
        assertEquals(123L, c.getId());
        c.setId(null);
        assertNull(c.getId());
    }
}
