package com.example.onlineStore.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DiscountTest {
    @Test
    void testDiscountSimple() {
        // startDate 1 segundo atras
        Date startDate = new Date(System.currentTimeMillis() - 1000);
        // endDate 5 días adelante
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 5);
        Date endDate = cal.getTime();

        Discount discount = new Discount("D001", "Promo10", new BigDecimal("0.10"), startDate, endDate);

        assertEquals("D001", discount.getIdDiscount());
        assertEquals("Promo10", discount.getNameDiscount());
        assertEquals(new BigDecimal("0.10"), discount.getPercentage());
        assertEquals(startDate, discount.getStartDate());
        assertEquals(endDate, discount.getEndDate());

        assertTrue(discount.isActive());
    }

}
