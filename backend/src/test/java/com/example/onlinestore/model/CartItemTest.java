package com.example.onlinestore.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CartItem model unit tests")
class CartItemTest {

    @Test
    @DisplayName("getId: por defecto debe ser null (generado por JPA)")
    void getId_defaultIsNull() {
        CartItem item = new CartItem();
        assertNull(item.getId(), "El id debería ser null antes de ser persistido por JPA");
    }

    @Test
    @DisplayName("get/setCart: debe asignar y retornar la misma referencia")
    void cart_getterSetter() {
        CartItem item = new CartItem();
        ShoppingCart cart = Mockito.mock(ShoppingCart.class);

        assertNull(item.getCart(), "Por defecto cart es null");

        item.setCart(cart);
        assertSame(cart, item.getCart(), "getCart() debe retornar la misma referencia asignada");

        item.setCart(null);
        assertNull(item.getCart(), "Debe aceptar null sin lanzar excepción");
    }

    @Test
    @DisplayName("get/setProduct: debe asignar y retornar la misma referencia")
    void product_getterSetter() {
        CartItem item = new CartItem();
        Product product = Mockito.mock(Product.class);

        assertNull(item.getProduct(), "Por defecto product es null");

        item.setProduct(product);
        assertSame(product, item.getProduct(), "getProduct() debe retornar la misma referencia asignada");

        item.setProduct(null);
        assertNull(item.getProduct(), "Debe aceptar null sin lanzar excepción");
    }

    @Test
    @DisplayName("get/setQuantity: debe asignar y retornar el valor establecido")
    void quantity_getterSetter() {
        CartItem item = new CartItem();

        // valor por defecto
        assertEquals(0, item.getQuantity(), "Por defecto quantity debería ser 0");

        item.setQuantity(3);
        assertEquals(3, item.getQuantity());

        item.setQuantity(0);
        assertEquals(0, item.getQuantity());
    }

    @Test
    @DisplayName("get/setCouponCode: debe asignar y retornar el código")
    void couponCode_getterSetter() {
        CartItem item = new CartItem();

        assertNull(item.getCouponCode(), "Por defecto couponCode es null");

        item.setCouponCode("SALE10");
        assertEquals("SALE10", item.getCouponCode());

        item.setCouponCode(null);
        assertNull(item.getCouponCode(), "Debe aceptar null sin lanzar excepción");
    }

    @Test
    @DisplayName("get/setCouponPct: debe asignar y retornar el porcentaje")
    void couponPct_getterSetter() {
        CartItem item = new CartItem();

        assertNull(item.getCouponPct(), "Por defecto couponPct es null");

        item.setCouponPct(new BigDecimal("0.15"));
        assertEquals(new BigDecimal("0.15"), item.getCouponPct());

        item.setCouponPct(null);
        assertNull(item.getCouponPct(), "Debe aceptar null sin lanzar excepción");
    }

    @Test
    @DisplayName("hasCoupon: null o <= 0 debe ser false; > 0 debe ser true")
    void hasCoupon_logic() {
        CartItem item = new CartItem();

        // null -> false
        item.setCouponPct(null);
        assertFalse(item.hasCoupon(), "null debe ser false");

        // 0 -> false
        item.setCouponPct(BigDecimal.ZERO);
        assertFalse(item.hasCoupon(), "0 debe ser false");

        // negativo -> false
        item.setCouponPct(new BigDecimal("-0.10"));
        assertFalse(item.hasCoupon(), "negativo debe ser false");

        // positivo -> true
        item.setCouponPct(new BigDecimal("0.10"));
        assertTrue(item.hasCoupon(), "> 0 debe ser true");
    }
}
