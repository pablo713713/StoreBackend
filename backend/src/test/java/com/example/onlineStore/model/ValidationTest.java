package com.example.onlineStore.model;


import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    @Test
    void testValidCreditCardNumber() {
        assertTrue(Validation.isValidCreditCardNumber("1234567812345678"));
        assertFalse(Validation.isValidCreditCardNumber("123"));
    }

    @Test
    void testValidPayPalEmail() {
        assertTrue(Validation.isValidPayPalEmail("user@example.com"));
        assertFalse(Validation.isValidPayPalEmail("invalid-email"));
    }

    @Test
    void testValidAmount() {
        assertTrue(Validation.isValidAmount(BigDecimal.valueOf(100)));
        assertFalse(Validation.isValidAmount(BigDecimal.valueOf(-5)));
    }

    @Test
    void testHasRequiredParams() {
        String[] params = {"a", "b"};
        assertTrue(Validation.hasRequiredParams(params, 2));
        assertFalse(Validation.hasRequiredParams(params, 3));
    }
}
