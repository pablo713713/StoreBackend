package com.example.onlinestore.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Validation utility unit tests")
class ValidationTest {

    // ---------- constructor privado ----------
    @Test
    @DisplayName("Constructor privado lanza UnsupportedOperationException (envuelta en InvocationTargetException)")
    void privateConstructor_shouldThrowWrapped() throws Exception {
        Constructor<Validation> ctor = Validation.class.getDeclaredConstructor();
        ctor.setAccessible(true);

        java.lang.reflect.InvocationTargetException ex =
                assertThrows(java.lang.reflect.InvocationTargetException.class, ctor::newInstance);

        assertNotNull(ex.getCause());
        assertTrue(ex.getCause() instanceof UnsupportedOperationException);
        assertEquals("Utility class", ex.getCause().getMessage());
    }

    // ---------- isValidUsername ----------
    @Test
    @DisplayName("isValidUsername: null y <=4 false; >=5 true")
    void isValidUsername_cases() {
        assertFalse(Validation.isValidUsername(null));
        assertFalse(Validation.isValidUsername("abcd"));   // 4
        assertTrue(Validation.isValidUsername("abcde"));   // 5
        assertTrue(Validation.isValidUsername("username123"));
    }

    // ---------- isValidPassword ----------
    @Test
    @DisplayName("isValidPassword: longitud y combinación letra+dígito")
    void isValidPassword_cases() {
        assertFalse(Validation.isValidPassword(null));
        assertFalse(Validation.isValidPassword("abcd"));           // muy corto
        assertFalse(Validation.isValidPassword("abcde"));          // sin dígitos
        assertFalse(Validation.isValidPassword("12345"));          // sin letras
        assertTrue(Validation.isValidPassword("abc12"));           // válido

        // >128 chars -> false
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 129; i++) sb.append(i % 10);
        assertFalse(Validation.isValidPassword(sb.toString()));
    }

    // ---------- isTimeGone / isNotTimeGone ----------
    @Test
    @DisplayName("isTimeGone: pasada true, futura false, null false")
    void isTimeGone_cases() {
        Date past = new Date(System.currentTimeMillis() - 24*3600_000L);
        Date future = new Date(System.currentTimeMillis() + 24*3600_000L);
        assertTrue(Validation.isTimeGone(past));
        assertFalse(Validation.isTimeGone(future));
        assertFalse(Validation.isTimeGone(null));
    }

    @Test
    @DisplayName("isNotTimeGone: es el negado de isTimeGone")
    void isNotTimeGone_cases() {
        Date past = new Date(System.currentTimeMillis() - 24*3600_000L);
        Date future = new Date(System.currentTimeMillis() + 24*3600_000L);
        assertFalse(Validation.isNotTimeGone(past));
        assertTrue(Validation.isNotTimeGone(future));
        assertTrue(Validation.isNotTimeGone(null));
    }

    // ---------- isValidAmount ----------
    @Test
    @DisplayName("isValidAmount: null/<=0 false; >0 true")
    void isValidAmount_cases() {
        assertFalse(Validation.isValidAmount(null));
        assertFalse(Validation.isValidAmount(new BigDecimal("0")));
        assertFalse(Validation.isValidAmount(new BigDecimal("-1")));
        assertTrue(Validation.isValidAmount(new BigDecimal("0.01")));
    }

    // ---------- isValidCreditCardNumber ----------
    @Test
    @DisplayName("isValidCreditCardNumber: exactamente 16 dígitos")
    void isValidCreditCardNumber_cases() {
        assertFalse(Validation.isValidCreditCardNumber(null));
        assertFalse(Validation.isValidCreditCardNumber("1234"));
        assertFalse(Validation.isValidCreditCardNumber("12345678abcdefgh"));
        assertTrue(Validation.isValidCreditCardNumber("1234567812345678"));
    }

    // ---------- isValidPayPalEmail ----------
    @Test
    @DisplayName("isValidPayPalEmail: debe contener @")
    void isValidPayPalEmail_cases() {
        assertFalse(Validation.isValidPayPalEmail(null));
        assertFalse(Validation.isValidPayPalEmail("correo"));
        assertTrue(Validation.isValidPayPalEmail("correo@mail.com"));
    }

    // ---------- hasRequiredParams ----------
    @Test
    @DisplayName("hasRequiredParams: params != null y length >= expected")
    void hasRequiredParams_cases() {
        assertFalse(Validation.hasRequiredParams(null, 2));
        assertFalse(Validation.hasRequiredParams(new String[]{"a"}, 2));
        assertTrue(Validation.hasRequiredParams(new String[]{"a","b"}, 2));
        assertTrue(Validation.hasRequiredParams(new String[]{"a","b","c"}, 2));
    }

    // ---------- hasStarted / notExpired ----------
    @Test
    @DisplayName("hasStarted: null y pasado true; futuro false")
    void hasStarted_cases() {
        Date past = new Date(System.currentTimeMillis() - 1000L);
        Date future = new Date(System.currentTimeMillis() + 1000L);
        assertTrue(Validation.hasStarted(null));
        assertTrue(Validation.hasStarted(past));
        assertFalse(Validation.hasStarted(future));
    }

    @Test
    @DisplayName("notExpired: null y futuro true; pasado false")
    void notExpired_cases() {
        Date past = new Date(System.currentTimeMillis() - 1000L);
        Date future = new Date(System.currentTimeMillis() + 1000L);
        assertTrue(Validation.notExpired(null));
        assertTrue(Validation.notExpired(future));
        assertFalse(Validation.notExpired(past));
    }

    // ---------- isValidPercentage ----------
    @Test
    @DisplayName("isValidPercentage: 0< pct ≤100")
    void isValidPercentage_cases() {
        assertFalse(Validation.isValidPercentage(null));
        assertFalse(Validation.isValidPercentage(new BigDecimal("0")));
        assertFalse(Validation.isValidPercentage(new BigDecimal("-1")));
        assertTrue(Validation.isValidPercentage(new BigDecimal("0.01")));
        assertTrue(Validation.isValidPercentage(new BigDecimal("100")));
        assertFalse(Validation.isValidPercentage(new BigDecimal("100.01")));
        assertFalse(Validation.isValidPercentage(new BigDecimal("150")));
    }
}
 