package com.example.onlineStore.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    @Test
    void testAddProductsToCart() {
        // Crear carrito
        ShoppingCart cart = new ShoppingCart();

        // Crear productos
        Product p1 = new Product("P001", "Laptop", "Gaming Laptop", new BigDecimal("1500.00"), 10);
        Product p2 = new Product("P002", "Mouse", "Wireless Mouse", new BigDecimal("50.00"), 20);

        // Agregar productos al carrito
        cart.addProduct(p1, 2);
        cart.addProduct(p2, 3);

        // Validaciones
        assertEquals(2, cart.getItems().size());           // Dos productos diferentes
        assertEquals(2, cart.getItems().get(p1));         // Cantidad de p1
        assertEquals(3, cart.getItems().get(p2));         // Cantidad de p2
    }

    @Test
    void testRemoveProductFromCart() {
        // Crear carrito
        ShoppingCart cart = new ShoppingCart();

        // Crear producto
        Product p1 = new Product("P001", "Laptop", "Gaming Laptop", new BigDecimal("1500.00"), 10);

        // Agregar y luego eliminar producto
        cart.addProduct(p1, 2);
        cart.removeProduct(p1);

        // Validaciones
        assertEquals(0, cart.getItems().size());          // Carrito vacío
        assertFalse(cart.getItems().containsKey(p1));    // p1 no debe existir
    }
}
