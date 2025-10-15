package com.example.onlinestore.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PayPalPayment model unit tests")
class PayPalPaymentTest {

    @Test
    @DisplayName("Constructor con argumentos debe inicializar email y password")
    void constructor_shouldInitializeFields() {
        PayPalPayment pm = new PayPalPayment("user@example.com", "securePass");
        assertNotNull(pm);
        assertEquals("user@example.com", pm.getEmailAccount());
        assertEquals("securePass", pm.getPassword());
    }

    @Test
    @DisplayName("get/setEmailAccount deben asignar y devolver el valor")
    void emailAccount_getterSetter() {
        PayPalPayment pm = new PayPalPayment("a@b.com", "x");
        assertEquals("a@b.com", pm.getEmailAccount());

        pm.setEmailAccount("nuevo@paypal.com");
        assertEquals("nuevo@paypal.com", pm.getEmailAccount());

        pm.setEmailAccount(null);
        assertNull(pm.getEmailAccount());
    }

    @Test
    @DisplayName("get/setPassword deben asignar y devolver el valor")
    void password_getterSetter() {
        PayPalPayment pm = new PayPalPayment("a@b.com", "pass123");
        assertEquals("pass123", pm.getPassword());

        pm.setPassword("nuevaClave");
        assertEquals("nuevaClave", pm.getPassword());

        pm.setPassword(null);
        assertNull(pm.getPassword());
    }

    @Test
    @DisplayName("pay siempre debe devolver true (simulación de pago exitoso)")
    void pay_shouldAlwaysReturnTrue() {
        PayPalPayment pm = new PayPalPayment("a@b.com", "x");

        assertTrue(pm.pay(new BigDecimal("100.00")));
        assertTrue(pm.pay(BigDecimal.ZERO));
        assertTrue(pm.pay(new BigDecimal("-5")));   // negativo
        assertTrue(pm.pay(null));                   // nulo (no se usa en la implementación actual)
    }

    @Test
    @DisplayName("getPaymentDetails debe incluir el correo configurado")
    void getPaymentDetails_shouldIncludeEmail() {
        PayPalPayment pm = new PayPalPayment("john@paypal.com", "x");
        assertEquals("PayPal account: john@paypal.com", pm.getPaymentDetails());

        pm.setEmailAccount(null);
        assertEquals("PayPal account: null", pm.getPaymentDetails());
    }
}
