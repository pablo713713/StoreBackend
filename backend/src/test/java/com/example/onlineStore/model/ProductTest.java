package com.example.onlineStore.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testProductCreationAndGettersSetters() {
        Product product = new Product("P001", "Laptop", "Gaming Laptop", new BigDecimal("1500.00"), 10);

        // Validar creación y getters
        assertEquals("P001", product.getIdProduct());
        assertEquals("Laptop", product.getNameProduct());
        assertEquals("Gaming Laptop", product.getDescription());
        assertEquals(new BigDecimal("1500.00"), product.getPrice());
        assertEquals(10, product.getStock());

        // Probar setters
        product.setNameProduct("Laptop Updated");
        product.setDescription("Updated Description");
        product.setPrice(new BigDecimal("1600.00"));
        product.setStock(15);

        assertEquals("Laptop Updated", product.getNameProduct());
        assertEquals("Updated Description", product.getDescription());
        assertEquals(new BigDecimal("1600.00"), product.getPrice());
        assertEquals(15, product.getStock());
    }

    @Test
    void testProductWithDiscount() {
        Product product = new Product("P002", "Mouse", "Wireless Mouse", new BigDecimal("50.00"), 20);

        // StartDate 1 segundo antes para asegurar que isActive() sea true
        Date startDate = new Date(System.currentTimeMillis() - 1000);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 5);
        Date endDate = cal.getTime();

        // Crear descuento 10%
        Discount discount = new Discount("D1","Small Offer",new BigDecimal("0.10"), startDate,endDate);
        product.setDiscount(discount);

        // Validar descuento aplicado con 2 decimales
        BigDecimal expectedPrice = new BigDecimal("45.00").setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal actualPrice = product.getPriceWithDiscount().setScale(2, BigDecimal.ROUND_HALF_UP);

        assertEquals(expectedPrice, actualPrice);
    }



    @Test
    void testStockOperations() {
        Product product = new Product("P003", "Keyboard", "Mechanical Keyboard", new BigDecimal("120.00"), 10);

        // Reducir stock
        product.setStock(product.getStock() - 2);
        assertEquals(8, product.getStock());

        // Aumentar stock
        product.setStock(product.getStock() + 5);
        assertEquals(13, product.getStock());
    }
}
