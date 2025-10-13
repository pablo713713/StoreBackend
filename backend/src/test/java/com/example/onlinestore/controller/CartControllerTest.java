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

    @Test
    void testAddItem() {
        CartController.AddItemRequest req = new CartController.AddItemRequest();
        req.setCartId(1L);
        req.setProductId(2L);
        req.setQuantity(3);

        CartItem item = mockCartItem(2L, "Producto X", 3, BigDecimal.valueOf(20));
        when(service.addItem(1L, 2L, 3)).thenReturn(item);

        ResponseEntity<CartItemDTO> response = controller.addItem(req);

        assertEquals(URI.create("/api/cart/1"), response.getHeaders().getLocation());
        assertEquals("Producto X", response.getBody().getNameProduct());
        assertEquals(BigDecimal.valueOf(20), response.getBody().getPrice());
    }

    @Test
    void testUpdateQuantity() {
        CartController.UpdateQuantityRequest req = new CartController.UpdateQuantityRequest();
        req.setQuantity(5);

        CartItem updated = mockCartItem(3L, "Actualizado", 5, BigDecimal.valueOf(15));
        when(service.updateQuantity(10L, 5)).thenReturn(updated);

        ResponseEntity<CartItemDTO> response = controller.updateQuantity(10L, req);

        assertEquals("Actualizado", response.getBody().getNameProduct());
        assertEquals(BigDecimal.valueOf(15), response.getBody().getPrice());
    }

    @Test
    void testUpdateQuantityByProduct() {
        CartController.UpdateQuantityRequest req = new CartController.UpdateQuantityRequest();
        req.setQuantity(7);

        ResponseEntity<Void> response = controller.updateQuantityByProduct(1L, 2L, req);

        assertEquals(204, response.getStatusCodeValue());
        verify(service).updateQuantityByProduct(1L, 2L, 7);
    }
    @Test
    void testRemoveByProduct() {
        ResponseEntity<Void> response = controller.removeByProduct(1L, 2L);
        assertEquals(204, response.getStatusCodeValue());
        verify(service).removeByProduct(1L, 2L);
    }
    @Test
    void testRemoveItem() {
        ResponseEntity<Void> response = controller.removeItem(5L);
        assertEquals(204, response.getStatusCodeValue());
        verify(service).removeItem(5L);
    }
    @Test
    void testClearCart() {
        ResponseEntity<Void> response = controller.clearCart(9L);
        assertEquals(204, response.getStatusCodeValue());
        verify(service).clearCart(9L);
    }


}
