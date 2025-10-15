
package com.example.onlinestore.service;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.onlinestore.model.CartItem;
import com.example.onlinestore.repository.CartItemRepository;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {
    
    @Mock
    private CartItemRepository cartItemRepo;

    @InjectMocks
    private CartItemService cartItemService;
    private static final Long CART_ID = 1L;

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
}
