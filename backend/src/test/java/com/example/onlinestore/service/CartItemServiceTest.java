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
import com.example.onlinestore.model.Discount;
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

    @Mock
    private DiscountService discountService;

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

    //tests for updateQuantityByProduct method

    @Test
    void updateQuantityByProductCantidadInvalida() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            cartItemService.updateQuantityByProduct(CART_ID, PRODUCT_ID, 0)
        );
        assertTrue(ex.getMessage().contains("quantity must be > 0"));
    }

    @Test
    void updateQuantityByProductItemNoExiste() {
        when(cartItemRepo.findByCart_IdAndProduct_Id(CART_ID, PRODUCT_ID)).thenReturn(Optional.empty());
        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
            cartItemService.updateQuantityByProduct(CART_ID, PRODUCT_ID, QTY)
        );
        assertTrue(ex.getMessage().contains("CartItem not found"));
    }

    @Test
    void updateQuantityByProductActualizaCorrectamente() {
        CartItem item = mock(CartItem.class);
        when(cartItemRepo.findByCart_IdAndProduct_Id(CART_ID, PRODUCT_ID)).thenReturn(Optional.of(item));

        cartItemService.updateQuantityByProduct(CART_ID, PRODUCT_ID, 7);

        verify(item).setQuantity(7);
        verify(cartItemRepo).save(item);
    }

    //tests for removeItem method

    @Test
    void removeItemEliminaPorId() {
        cartItemService.removeItem(123L);
        verify(cartItemRepo).deleteById(123L);
    }

    //tests for removeByProduct method

    @Test
    void removeByProductItemNoExiste() {
        when(cartItemRepo.findByCart_IdAndProduct_Id(CART_ID, PRODUCT_ID)).thenReturn(Optional.empty());
        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
            cartItemService.removeByProduct(CART_ID, PRODUCT_ID)
        );
        assertTrue(ex.getMessage().contains("CartItem not found"));
    }

    @Test
    void removeByProductEliminaCorrectamente() {
        CartItem item = mock(CartItem.class);
        when(cartItemRepo.findByCart_IdAndProduct_Id(CART_ID, PRODUCT_ID)).thenReturn(Optional.of(item));
        when(item.getId()).thenReturn(555L);

        cartItemService.removeByProduct(CART_ID, PRODUCT_ID);

        verify(cartItemRepo).deleteById(555L);
    }

    //tests for clearCart method

    @Test
    void clearCartEliminaPorCartId() {
        cartItemService.clearCart(CART_ID);
        verify(cartItemRepo).deleteByCart_Id(CART_ID);
    }

    //tests for applyCode method

    @Test
    void applyCodeItemNoExiste() {
        when(cartItemRepo.findByCart_IdAndProduct_Id(CART_ID, PRODUCT_ID)).thenReturn(Optional.empty());
        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
            cartItemService.applyCode(CART_ID, PRODUCT_ID, "PROMO10")
        );
        assertTrue(ex.getMessage().contains("CartItem not found"));
    }

    @Test
    void applyCodeCodigoInvalido() {
        CartItem item = mock(CartItem.class);
        Product product = mock(Product.class);
        when(cartItemRepo.findByCart_IdAndProduct_Id(CART_ID, PRODUCT_ID)).thenReturn(Optional.of(item));
        when(item.getProduct()).thenReturn(product);
        when(discountService.validateForProduct("PROMO10", product)).thenReturn(Optional.empty());

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
            cartItemService.applyCode(CART_ID, PRODUCT_ID, "PROMO10")
        );
        assertTrue(ex.getMessage().contains("inválido"));
    }

    @Test
    void applyCodeAplicaCorrectamente() {
        CartItem item = mock(CartItem.class);
        Product product = mock(Product.class);
        Discount discount = mock(Discount.class);
        when(cartItemRepo.findByCart_IdAndProduct_Id(CART_ID, PRODUCT_ID)).thenReturn(Optional.of(item));
        when(item.getProduct()).thenReturn(product);
        when(discountService.validateForProduct("PROMO10", product)).thenReturn(Optional.of(discount));
        when(discount.getIdDiscount()).thenReturn("PROMO10");
        when(discount.getPercentage()).thenReturn(new java.math.BigDecimal("0.15"));
        when(cartItemRepo.save(item)).thenReturn(item);

        CartItem result = cartItemService.applyCode(CART_ID, PRODUCT_ID, "PROMO10");

        verify(item).setCouponCode("PROMO10");
        verify(item).setCouponPct(new java.math.BigDecimal("0.15"));
        verify(cartItemRepo).save(item);
        assertEquals(item, result);
    }

    //tests for removeCode method

    @Test
    void removeCodeItemNoExiste() {
        when(cartItemRepo.findByCart_IdAndProduct_Id(CART_ID, PRODUCT_ID)).thenReturn(Optional.empty());
        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
            cartItemService.removeCode(CART_ID, PRODUCT_ID)
        );
        assertTrue(ex.getMessage().contains("CartItem not found"));
    }

    @Test
    void removeCodeEliminaCorrectamente() {
        CartItem item = mock(CartItem.class);
        when(cartItemRepo.findByCart_IdAndProduct_Id(CART_ID, PRODUCT_ID)).thenReturn(Optional.of(item));
        when(cartItemRepo.save(item)).thenReturn(item);

        CartItem result = cartItemService.removeCode(CART_ID, PRODUCT_ID);

        verify(item).setCouponCode(null);
        verify(item).setCouponPct(null);
        verify(cartItemRepo).save(item);
        assertEquals(item, result);
    }

    //test for efectiveUnitPrice method

    @Test
    void efectiveUnitPriceSinDescuento() {
        CartItem item = mock(CartItem.class);
        Product product = mock(Product.class);
        when(item.getProduct()).thenReturn(product);
        when(product.getPrice()).thenReturn(new java.math.BigDecimal("100.00"));
        when(item.getCouponPct()).thenReturn(null);

        java.math.BigDecimal result = cartItemService.effectiveUnitPrice(item);
        assertEquals(new java.math.BigDecimal("100.00"), result);
    }

    @Test
    void efectiveUnitPriceConDescuento() {
        CartItem item = mock(CartItem.class);
        Product product = mock(Product.class);
        when(item.getProduct()).thenReturn(product);
        when(product.getPrice()).thenReturn(new java.math.BigDecimal("200.00"));
        when(item.getCouponPct()).thenReturn(new java.math.BigDecimal("0.20"));
        when(discountService.applyPct(new java.math.BigDecimal("200.00"), new java.math.BigDecimal("0.20")))
            .thenReturn(new java.math.BigDecimal("160.00"));

        java.math.BigDecimal result = cartItemService.effectiveUnitPrice(item);
        assertEquals(new java.math.BigDecimal("160.00"), result);
    }
}
