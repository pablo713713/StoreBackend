package com.example.onlinestore.controller;

import com.example.onlinestore.dto.CartItemDTO;
import com.example.onlinestore.model.CartItem;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.model.ShoppingCart;
import com.example.onlinestore.repository.ShoppingCartRepository;
import com.example.onlinestore.service.CartItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartControllerTest {

    private CartItemService service;
    private ShoppingCartRepository cartRepo;
    private CartController controller;

    @BeforeEach
    void setUp() {
        service = mock(CartItemService.class);
        cartRepo = mock(ShoppingCartRepository.class);
        controller = new CartController(service, cartRepo);
    }

    // Helper: crea mocks para un CartItem simulado
    private CartItem mockCartItem(Long id, String name, int quantity, BigDecimal price) {
        Product product = mock(Product.class);
        when(product.getId()).thenReturn(id);
        when(product.getNameProduct()).thenReturn(name);

        CartItem item = mock(CartItem.class);
        when(item.getProduct()).thenReturn(product);
        when(item.getQuantity()).thenReturn(quantity);

        // Simula el cálculo del precio unitario
        when(service.effectiveUnitPrice(item)).thenReturn(price);

        return item;
    }

    @Test
    void testGetCart() {
        CartItem item = mockCartItem(1L, "Producto A", 2, BigDecimal.valueOf(10));
        when(service.getCart(1L)).thenReturn(List.of(item));

        List<CartItemDTO> result = controller.getCart(1L);

        assertEquals(1, result.size());
        assertEquals("Producto A", result.get(0).getNameProduct());
        assertEquals(2, result.get(0).getQty());
        assertEquals(BigDecimal.valueOf(10), result.get(0).getPrice());
    }

    @Test
    void testEnsureCart_NullCartId_CreatesNew() {
        ShoppingCart newCart = mock(ShoppingCart.class);
        when(newCart.getId()).thenReturn(100L);
        when(cartRepo.save(any())).thenReturn(newCart);

        ResponseEntity<Long> response = controller.ensureCart(null);

        assertEquals(100L, response.getBody());
        verify(cartRepo).save(any());
    }
    @Test
    void testEnsureCart_ExistingCart_ReturnsSame() {
        ShoppingCart existing = mock(ShoppingCart.class);
        when(existing.getId()).thenReturn(1L);
        when(cartRepo.findById(1L)).thenReturn(Optional.of(existing));

        ResponseEntity<Long> response = controller.ensureCart(1L);

        assertEquals(1L, response.getBody());
        verify(cartRepo, never()).save(any());
    }

    @Test
    void testEnsureCart_NotFound_CreatesNew() {
        when(cartRepo.findById(1L)).thenReturn(Optional.empty());
        ShoppingCart created = mock(ShoppingCart.class);
        when(created.getId()).thenReturn(200L);
        when(cartRepo.save(any())).thenReturn(created);

        ResponseEntity<Long> response = controller.ensureCart(1L);

        assertEquals(200L, response.getBody());
        verify(cartRepo).save(any());
    }


}
