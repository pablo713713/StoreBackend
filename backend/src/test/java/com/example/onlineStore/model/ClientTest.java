package com.example.onlineStore.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void testClientCreation() {
        Client client = new Client(
                "Diego",
                "Rios",
                "Valverde",
                "C003",
                "mail@example.com",
                "password123"
        );

        ShoppingCart cart = new ShoppingCart();
        client.setCart(cart);

        assertNotNull(client);
        assertNotNull(client.getCart());
        assertEquals("mail@example.com", client.getEmail());
    }

    @Test
    void testClientWithPayPalPayment() {
        Client client = new Client(
                "Diego",
                "Rios",
                "Valverde",
                "C003",
                "mail@example.com",
                "password123"
        );

        // Creamos el Payment directamente
        Payment payment = new CreatorPayPalPayment(
                new BigDecimal("100.00"),
                "correo@ejemplo.com",
                "password123"
        );

        client.setPaymentMethod(payment);

        assertNotNull(client.getPaymentMethod());
        assertTrue(client.getPaymentMethod().pay());
        assertEquals("PayPal account: correo@ejemplo.com", client.getPaymentMethod().getPaymentDetails());
    }

    @Test
    void testClientWithCreditCardPayment() {
        Client client = new Client(
                "Diego",
                "Rios",
                "Valverde",
                "C003",
                "mail@example.com",
                "password123"
        );

        Payment payment = new CreatorCreditCardPayment(
                new BigDecimal("200.00"),
                "4444555566667777",
                "Diego Rios",
                "12/25",
                "123"
        );

        client.setPaymentMethod(payment);

        assertNotNull(client.getPaymentMethod());
        assertTrue(client.getPaymentMethod().pay());
        assertEquals("CreditCard ****7777", client.getPaymentMethod().getPaymentDetails());
    }
}
