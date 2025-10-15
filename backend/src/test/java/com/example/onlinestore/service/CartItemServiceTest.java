package com.example.onlinestore.service;
import java.util.List;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.onlinestore.model.CartItem;
import com.example.onlinestore.repository.CartItemRepository;

import com.example.onlinestore.model.ShoppingCart;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.ShoppingCartRepository;
import com.example.onlinestore.repository.ProductRepository;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {
    
    @Mock
    private CartItemRepository cartItemRepo;

    @Mock
    private ShoppingCartRepository cartRepo;

    @Mock
    private ProductRepository productRepo;

    @InjectMocks
    private CartItemService cartItemService;
    private static final Long CART_ID = 1L;

    private static final Long PRODUCT_ID = 10L;
    private static final int QTY = 3;

    @BeforeEach
    void setUp() {
    }

//tests for getCart method

    @Test
    void getCartRetornaItemsDelCarrito() {
        CartItem item1 = mock(CartItem.class);
        CartItem item2 = mock(CartItem.class);
        List<CartItem> items = List.of(item1, item2);
        when(cartItemRepo.findByCart_Id(CART_ID)).thenReturn(items);

        List<CartItem> result = cartItemService.getCart(CART_ID);

        assertEquals(2, result.size());
        assertTrue(result.contains(item1));
        assertTrue(result.contains(item2));
        verify(cartItemRepo).findByCart_Id(CART_ID);
    }

    //tests for addItem method

    @Test
    void addItemCantidadInvalida() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            cartItemService.addItem(CART_ID, PRODUCT_ID, 0)
        );
    assertTrue(ex.getMessage().contains("quantity must be > 0"));
    }

    @Test
    void addItemProductoNoExiste() {
        ShoppingCart cart = mock(ShoppingCart.class);
        when(cartRepo.findById(CART_ID)).thenReturn(Optional.of(cart));
        when(productRepo.findById(PRODUCT_ID)).thenReturn(Optional.empty());
        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
            cartItemService.addItem(CART_ID, PRODUCT_ID, QTY)
        );
        assertTrue(ex.getMessage().contains("Product not found"));
    }

    @Test
    void addItemSumaCantidadSiYaExiste() {
        ShoppingCart cart = mock(ShoppingCart.class);
        Product product = mock(Product.class);
        CartItem existing = mock(CartItem.class);
        when(cartRepo.findById(CART_ID)).thenReturn(Optional.of(cart));
        when(productRepo.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(cart.getId()).thenReturn(CART_ID);
        when(cartItemRepo.findByCart_IdAndProduct_Id(CART_ID, PRODUCT_ID)).thenReturn(Optional.of(existing));
        when(existing.getQuantity()).thenReturn(2);
        when(cartItemRepo.save(existing)).thenReturn(existing);

        CartItem result = cartItemService.addItem(CART_ID, PRODUCT_ID, QTY);

        verify(existing).setQuantity(5);
        assertEquals(existing, result);
        verify(cartItemRepo).save(existing);
    }

    @Test
    void addItemCreaNuevoSiNoExiste() {
        ShoppingCart cart = mock(ShoppingCart.class);
        Product product = mock(Product.class);
        when(cartRepo.findById(CART_ID)).thenReturn(Optional.of(cart));
        when(productRepo.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(cart.getId()).thenReturn(CART_ID);
        when(cartItemRepo.findByCart_IdAndProduct_Id(CART_ID, PRODUCT_ID)).thenReturn(Optional.empty());

        // Captura el CartItem que se guarda
        ArgumentCaptor<CartItem> captor = ArgumentCaptor.forClass(CartItem.class);
        when(cartItemRepo.save(any(CartItem.class))).thenAnswer(inv -> inv.getArgument(0));

        CartItem result = cartItemService.addItem(CART_ID, PRODUCT_ID, QTY);
        verify(cartItemRepo).save(captor.capture());
        CartItem saved = captor.getValue();
        assertSame(saved, result);
        assertEquals(cart, saved.getCart());
        assertEquals(product, saved.getProduct());
        assertEquals(QTY, saved.getQuantity());
    }

    //Tests for updateQuantity method

    @Test
    void updateQuantityCantidadInvalida() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            cartItemService.updateQuantity(99L, 0)
        );
        assertTrue(ex.getMessage().contains("quantity must be > 0"));
    }

    @Test
    void updateQuantityItemNoExiste() {
        when(cartItemRepo.findById(99L)).thenReturn(Optional.empty());
        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
            cartItemService.updateQuantity(99L, 2)
        );
        assertTrue(ex.getMessage().contains("CartItem not found"));
    }

    @Test
    void updateQuantityActualizaCorrectamente() {
        CartItem item = mock(CartItem.class);
        when(cartItemRepo.findById(99L)).thenReturn(Optional.of(item));
        when(cartItemRepo.save(item)).thenReturn(item);

        CartItem result = cartItemService.updateQuantity(99L, 5);

        verify(item).setQuantity(5);
        verify(cartItemRepo).save(item);
        assertEquals(item, result);
    }
}
