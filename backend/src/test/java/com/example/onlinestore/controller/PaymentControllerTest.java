package com.example.onlinestore.controller;

import com.example.onlinestore.model.Payment;
import com.example.onlinestore.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para el método createPayment
    @Test
    void testCreatePayment() {
        // Arrange
        Long clientId = 1L;
        PaymentController.PaymentRequest request = new PaymentController.PaymentRequest();
        request.setType("paypal");
        request.setAmount(BigDecimal.valueOf(100.0));
        request.setParams(new String[]{"test@example.com", "password123"});

        Payment mockPayment = mock(Payment.class);
        when(paymentService.createPaymentForClient(eq(clientId), eq("paypal"), eq(BigDecimal.valueOf(100.0)), eq(new String[]{"test@example.com", "password123"})))
                .thenReturn(mockPayment);

        // Act
        Payment response = paymentController.createPayment(clientId, request);

        // Assert
        assertNotNull(response);
        verify(paymentService).createPaymentForClient(eq(clientId), eq("paypal"), eq(BigDecimal.valueOf(100.0)), eq(new String[]{"test@example.com", "password123"}));
    }

    // Test para el método makePayment
    @Test
    void testMakePaymentSuccess() {
        // Arrange
        Long clientId = 1L;
        when(paymentService.makePayment(clientId)).thenReturn(true);

        // Act
        String response = paymentController.makePayment(clientId);

        // Assert
        assertEquals("Pago realizado con éxito", response);
        verify(paymentService).makePayment(clientId);
    }

    @Test
    void testMakePaymentFailure() {
        // Arrange
        Long clientId = 1L;
        when(paymentService.makePayment(clientId)).thenReturn(false);

        // Act
        String response = paymentController.makePayment(clientId);

        // Assert
        assertEquals("Pago fallido", response);
        verify(paymentService).makePayment(clientId);
    }

    // Test para el método getPaymentDetails
    @Test
    void testGetPaymentDetails() {
        // Arrange
        Long clientId = 1L;
        String expectedDetails = "PayPal account: test@example.com";
        when(paymentService.getPaymentDetails(clientId)).thenReturn(expectedDetails);

        // Act
        String response = paymentController.getPaymentDetails(clientId);

        // Assert
        assertEquals(expectedDetails, response);
        verify(paymentService).getPaymentDetails(clientId);
    }

    // Test para cuando no se encuentra el cliente
    @Test
    void testCreatePayment_ClientNotFound() {
        // Arrange
        Long clientId = 1L;
        PaymentController.PaymentRequest request = new PaymentController.PaymentRequest();
        request.setType("paypal");
        request.setAmount(BigDecimal.valueOf(100.0));
        request.setParams(new String[]{"test@example.com", "password123"});

        when(paymentService.createPaymentForClient(eq(clientId), eq("paypal"), eq(BigDecimal.valueOf(100.0)), eq(new String[]{"test@example.com", "password123"})))
                .thenThrow(new IllegalArgumentException("Cliente no encontrado"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentController.createPayment(clientId, request);
        });

        assertEquals("Cliente no encontrado", exception.getMessage());
    }
}
