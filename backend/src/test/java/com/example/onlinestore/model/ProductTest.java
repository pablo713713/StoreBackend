package com.example.onlinestore.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Product model unit tests")
class ProductTest {

    @Test
    @DisplayName("Constructor JPA sin argumentos: instancia válida y campos por defecto")
    void jpaConstructor_shouldCreateInstance() {
        Product p = new Product(); // mismo paquete => acceso permitido
        assertNotNull(p);

        assertNull(p.getId());
        assertNull(p.getIdProduct());
        assertNull(p.getNameProduct());
        assertNull(p.getDescription());
        assertNull(p.getPrice());
        assertEquals(0, p.getStock());
        assertNull(p.getCategory());
        assertNull(p.getDiscount());
        assertNull(p.getImageUrl());
    }

    @Test
    @DisplayName("Constructor con argumentos: inicializa todos los campos obligatorios")
    void argsConstructor_shouldInitializeFields() {
        BigDecimal price = new BigDecimal("123.45");
        Product p = new Product("PROD-1", "Paracetamol 500mg", "Analgesico", price, 10);

        assertNull(p.getId(), "id debe ser null hasta persistencia");
        assertEquals("PROD-1", p.getIdProduct());
        assertEquals("Paracetamol 500mg", p.getNameProduct());
        assertEquals("Analgesico", p.getDescription());
        assertEquals(price, p.getPrice());
        assertEquals(10, p.getStock());
        assertNull(p.getCategory());
        assertNull(p.getDiscount());
        assertNull(p.getImageUrl());
    }

    @Test
    @DisplayName("get/setIdProduct")
    void idProduct_getterSetter() {
        Product p = new Product();
        assertNull(p.getIdProduct());
        p.setIdProduct("SKU-123");
        assertEquals("SKU-123", p.getIdProduct());
        p.setIdProduct(null);
        assertNull(p.getIdProduct());
    }

    @Test
    @DisplayName("get/setNameProduct")
    void nameProduct_getterSetter() {
        Product p = new Product();
        assertNull(p.getNameProduct());
        p.setNameProduct("Ibuprofeno");
        assertEquals("Ibuprofeno", p.getNameProduct());
        p.setNameProduct(null);
        assertNull(p.getNameProduct());
    }

    @Test
    @DisplayName("get/setDescription")
    void description_getterSetter() {
        Product p = new Product();
        assertNull(p.getDescription());
        p.setDescription("Antiinflamatorio");
        assertEquals("Antiinflamatorio", p.getDescription());
        p.setDescription(null);
        assertNull(p.getDescription());
    }

    @Test
    @DisplayName("get/setPrice")
    void price_getterSetter() {
        Product p = new Product();
        assertNull(p.getPrice());
        p.setPrice(new BigDecimal("20.50"));
        assertEquals(new BigDecimal("20.50"), p.getPrice());
        p.setPrice(null);
        assertNull(p.getPrice());
    }

    @Test
    @DisplayName("get/setStock")
    void stock_getterSetter() {
        Product p = new Product();
        assertEquals(0, p.getStock());
        p.setStock(7);
        assertEquals(7, p.getStock());
        p.setStock(0);
        assertEquals(0, p.getStock());
    }

    @Test
    @DisplayName("get/setCategory")
    void category_getterSetter() {
        Product p = new Product();
        assertNull(p.getCategory());
        Category c = Mockito.mock(Category.class);
        p.setCategory(c);
        assertSame(c, p.getCategory());
        p.setCategory(null);
        assertNull(p.getCategory());
    }

    @Test
    @DisplayName("get/setDiscount")
    void discount_getterSetter() {
        Product p = new Product();
        assertNull(p.getDiscount());
        Discount d = Mockito.mock(Discount.class);
        p.setDiscount(d);
        assertSame(d, p.getDiscount());
        p.setDiscount(null);
        assertNull(p.getDiscount());
    }

    @Test
    @DisplayName("get/setImageUrl")
    void imageUrl_getterSetter() {
        Product p = new Product();
        assertNull(p.getImageUrl());
        p.setImageUrl("https://cdn/img.png");
        assertEquals("https://cdn/img.png", p.getImageUrl());
        p.setImageUrl(null);
        assertNull(p.getImageUrl());
    }

    @Test
    @DisplayName("getPriceWithDiscount: discount == null → retorna price")
    void priceWithDiscount_whenDiscountNull_returnsPrice() {
        Product p = new Product("ID", "Name", "Desc", new BigDecimal("100.00"), 1);
        p.setDiscount(null);
        assertEquals(0, p.getPriceWithDiscount().compareTo(new BigDecimal("100.00")));
    }

    @Test
    @DisplayName("getPriceWithDiscount: discount inactivo → retorna price")
    void priceWithDiscount_whenInactive_returnsPrice() {
        Product p = new Product("ID", "Name", "Desc", new BigDecimal("100.00"), 1);
        Discount d = mock(Discount.class);
        when(d.isActive()).thenReturn(false);
        p.setDiscount(d);

        assertEquals(0, p.getPriceWithDiscount().compareTo(new BigDecimal("100.00")));
    }

    @Test
    @DisplayName("getPriceWithDiscount: discount activo → price * (1 - percentage)")
    void priceWithDiscount_whenActive_appliesPercentage() {
        Product p = new Product("ID", "Name", "Desc", new BigDecimal("100.00"), 1);

        Discount d = mock(Discount.class);
        when(d.isActive()).thenReturn(true);
        when(d.getPercentage()).thenReturn(new BigDecimal("0.20")); // 20%

        p.setDiscount(d);

        // Esperado: 100.00 * (1 - 0.20) = 80.00
        BigDecimal expected = new BigDecimal("80.00");
        assertEquals(0, p.getPriceWithDiscount().compareTo(expected), "Debe aplicar 20% de descuento");

        // Verifica que el precio original NO cambia
        assertEquals(0, p.getPrice().compareTo(new BigDecimal("100.00")));
    }
}
