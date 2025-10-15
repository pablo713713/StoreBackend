package com.example.onlinestore.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CreditCardPayment model unit tests")
class CreditCardPaymentTest {

    @Test
    @DisplayName("Constructor con argumentos debe inicializar todos los campos")
    void constructor_shouldInitializeFields() {
        CreditCardPayment cc = new CreditCardPayment("4111111111111111", "Diego Rios", "12/27", "123");

        assertNotNull(cc);
        assertEquals("4111111111111111", cc.getCardNumber());
        assertEquals("Diego Rios", cc.getCardHolder());
        assertEquals("12/27", cc.getExpirationDate());
        assertEquals("123", cc.getCvv());
    }

    @Test
    @DisplayName("get/setCardNumber")
    void cardNumber_getterSetter() {
        CreditCardPayment cc = new CreditCardPayment(null, "H", "E", "C");

        assertNull(cc.getCardNumber());
        cc.setCardNumber("5555444433332222");
        assertEquals("5555444433332222", cc.getCardNumber());

        cc.setCardNumber(null);
        assertNull(cc.getCardNumber());
    }

    @Test
    @DisplayName("get/setCardHolder")
    void cardHolder_getterSetter() {
        CreditCardPayment cc = new CreditCardPayment("4111", null, "E", "C");

        assertNull(cc.getCardHolder());
        cc.setCardHolder("Alex Aguirre");
        assertEquals("Alex Aguirre", cc.getCardHolder());

        cc.setCardHolder(null);
        assertNull(cc.getCardHolder());
    }

    @Test
    @DisplayName("get/setExpirationDate")
    void expiration_getterSetter() {
        CreditCardPayment cc = new CreditCardPayment("4111", "H", null, "C");

        assertNull(cc.getExpirationDate());
        cc.setExpirationDate("08/30");
        assertEquals("08/30", cc.getExpirationDate());

        cc.setExpirationDate(null);
        assertNull(cc.getExpirationDate());
    }

    @Test
    @DisplayName("get/setCvv")
    void cvv_getterSetter() {
        CreditCardPayment cc = new CreditCardPayment("4111", "H", "E", null);

        assertNull(cc.getCvv());
        cc.setCvv("999");
        assertEquals("999", cc.getCvv());

        cc.setCvv(null);
        assertNull(cc.getCvv());
    }

    @Test
    @DisplayName("pay: con número válido (≥4 dígitos) siempre retorna true y no lanza excepción")
    void pay_shouldReturnTrue_andNotThrow_whenCardNumberValid() {
        CreditCardPayment cc = new CreditCardPayment("4111111111111111", "Diego", "12/27", "123");

        assertDoesNotThrow(() -> {
            boolean result = cc.pay(new BigDecimal("100.00"));
            assertTrue(result, "pay debe retornar true");
        });
    }

    @Test
    @DisplayName("getPaymentDetails: número válido → muestra últimos 4 dígitos")
    void getPaymentDetails_validNumber() {
        CreditCardPayment cc = new CreditCardPayment("1234567812345678", "Diego", "12/27", "123");
        String details = cc.getPaymentDetails();
        assertEquals("CreditCard ****5678", details);
    }

    @Test
    @DisplayName("getPaymentDetails: número con menos de 4 dígitos → '(número inválido)'")
    void getPaymentDetails_shortNumber_isInvalid() {
        CreditCardPayment cc = new CreditCardPayment("123", "Diego", "12/27", "123");
        String details = cc.getPaymentDetails();
        assertEquals("CreditCard (número inválido)", details);
    }

    @Test
    @DisplayName("getPaymentDetails: número null → '(número inválido)'")
    void getPaymentDetails_nullNumber_isInvalid() {
        CreditCardPayment cc = new CreditCardPayment(null, "Diego", "12/27", "123");
        String details = cc.getPaymentDetails();
        assertEquals("CreditCard (número inválido)", details);
    }
}
