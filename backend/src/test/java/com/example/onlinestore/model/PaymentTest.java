package com.example.onlinestore.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for abstract Payment using a concrete FakePayment subclass.
 */
@DisplayName("Payment (abstract) unit tests")
class PaymentTest {

    /** Subclase concreta para pruebas. */
    static class FakePayment extends Payment {
        private final IPaymentMethod provided;

        FakePayment(BigDecimal amount, IPaymentMethod provided) {
            super(amount);            // llama a createPaymentMethod()
            this.provided = provided; // guardamos para devolverlo desde createPaymentMethod()
            // remplazamos el method creado por super con el mock que queremos (consistencia):
            this.method = provided;
        }

        FakePayment() {
            super(); // ctor JPA
            this.provided = null;
        }

        @Override
        protected IPaymentMethod createPaymentMethod() {
            // El super(amount) invoca esto; devolvemos el mock que nos pasan
            return provided != null ? provided : Mockito.mock(IPaymentMethod.class);
        }
    }

    @Test
    @DisplayName("Ctor JPA vacío: id/amount/method deben quedar en null")
    void jpaCtor_shouldLeaveFieldsNull() {
        FakePayment p = new FakePayment();
        assertNull(p.getId(), "id debe ser null por defecto");
        assertNull(p.getAmount(), "amount debe ser null por defecto");
        // field protected; en misma package se puede acceder o usamos getPaymentDetails()
        assertEquals("No se ha asignado método de pago", p.getPaymentDetails());
    }

    @Test
    @DisplayName("Ctor con amount: asigna amount y crea método de pago vía createPaymentMethod()")
    void argsCtor_shouldAssignAmountAndCreateMethod() {
        IPaymentMethod mockMethod = mock(IPaymentMethod.class);
        FakePayment p = new FakePayment(new BigDecimal("50.00"), mockMethod);

        assertEquals(new BigDecimal("50.00"), p.getAmount(), "amount debe asignarse");
        // getPaymentDetails debe delegar al método provisto
        when(mockMethod.getPaymentDetails()).thenReturn("mock-details");
        assertEquals("mock-details", p.getPaymentDetails());
    }

    @Test
    @DisplayName("getId/set directo (campo público) debe reflejar el valor")
    void id_publicFieldShouldBeSettable() {
        FakePayment p = new FakePayment();
        assertNull(p.getId());
        p.id = 99L; // es público en la clase Payment
        assertEquals(99L, p.getId());
        p.id = null;
        assertNull(p.getId());
    }

    @Test
    @DisplayName("getAmount/setAmount deben asignar y devolver correctamente")
    void amount_getterSetter() {
        FakePayment p = new FakePayment();
        assertNull(p.getAmount());
        p.setAmount(new BigDecimal("123.45"));
        assertEquals(new BigDecimal("123.45"), p.getAmount());
        p.setAmount(null);
        assertNull(p.getAmount());
    }

    @Test
    @DisplayName("pay: si method es null, debe lanzar IllegalStateException")
    void pay_shouldThrowWhenNoMethod() {
        IPaymentMethod mockMethod = mock(IPaymentMethod.class);
        FakePayment p = new FakePayment(new BigDecimal("10.00"), mockMethod);
        // dejamos explícito el escenario sin método
        p.method = null; // protected; mismo paquete => permitido
        assertThrows(IllegalStateException.class, p::pay, "Debe lanzar si no hay método");
    }

    @Test
    @DisplayName("pay: delega a method.pay(amount) y retorna su resultado")
    void pay_shouldDelegateToMethod() {
        IPaymentMethod mockMethod = mock(IPaymentMethod.class);
        FakePayment p = new FakePayment(new BigDecimal("77.00"), mockMethod);

        when(mockMethod.pay(new BigDecimal("77.00"))).thenReturn(true);
        assertTrue(p.pay(), "Debe retornar true cuando el método retorna true");

        when(mockMethod.pay(new BigDecimal("77.00"))).thenReturn(false);
        assertFalse(p.pay(), "Debe retornar false cuando el método retorna false");
    }

    @Test
    @DisplayName("getPaymentDetails: si method es null devuelve el mensaje por defecto")
    void getPaymentDetails_whenNoMethod() {
        FakePayment p = new FakePayment();
        // sin método asignado
        assertEquals("No se ha asignado método de pago", p.getPaymentDetails());
    }

    @Test
    @DisplayName("getPaymentDetails: con method asignado, delega a method.getPaymentDetails()")
    void getPaymentDetails_shouldDelegate() {
        IPaymentMethod mockMethod = mock(IPaymentMethod.class);
        FakePayment p = new FakePayment(new BigDecimal("1.00"), mockMethod);

        when(mockMethod.getPaymentDetails()).thenReturn("PayPal account: test@x.com");
        assertEquals("PayPal account: test@x.com", p.getPaymentDetails());
    }
}
