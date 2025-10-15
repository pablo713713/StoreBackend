package com.example.onlinestore.service;

import com.example.onlinestore.model.Discount;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.DiscountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiscountServiceImplTest {

    private static final java.math.BigDecimal PRECIO = new java.math.BigDecimal("200.00");
    private static final java.math.BigDecimal PCT = new java.math.BigDecimal("15.00");
    private static final java.math.BigDecimal RESULTADO_ESPERADO = new java.math.BigDecimal("170.00");

    @InjectMocks
    private DiscountServiceImpl discountService;

    @BeforeEach
    void setUp() {
    }

    //tests for validateForProduct method

    @Mock
    private DiscountRepository discountRepo;

    @Test
    void validateForProductCodigoNullVacio() {
        Product product = mock(Product.class);
        assertTrue(discountService.validateForProduct(null, product).isEmpty());
    }

    @Test
    void validateForProductNoExisteEnRepo() {
        Product product = mock(Product.class);
        when(discountRepo.findByIdDiscount("PROMO10")).thenReturn(Optional.empty());
        assertTrue(discountService.validateForProduct("PROMO10", product).isEmpty());
    }

    @Test
    void validateForProductNoHaIniciado() {
        Product product = mock(Product.class);
        Discount discount = mock(Discount.class);
        when(discountRepo.findByIdDiscount("PROMO10")).thenReturn(Optional.of(discount));
        when(discount.getStartDate()).thenReturn(new java.util.Date(System.currentTimeMillis() + 1000000));
        assertTrue(discountService.validateForProduct("PROMO10", product).isEmpty());
    }

    @Test
    void validateForProductExpirado() {
        Product product = mock(Product.class);
        Discount discount = mock(Discount.class);
        when(discountRepo.findByIdDiscount("PROMO10")).thenReturn(Optional.of(discount));
        when(discount.getStartDate()).thenReturn(new java.util.Date(System.currentTimeMillis() - 2000000));
        when(discount.getEndDate()).thenReturn(new java.util.Date(System.currentTimeMillis() - 1000000));
        assertTrue(discountService.validateForProduct("PROMO10", product).isEmpty());
    }

    @Test
    void validateForProductPorcentajeInvalido() {
        Product product = mock(Product.class);
        Discount discount = mock(Discount.class);
        when(discountRepo.findByIdDiscount("PROMO10")).thenReturn(Optional.of(discount));
        when(discount.getStartDate()).thenReturn(new java.util.Date(System.currentTimeMillis() - 2000000));
        when(discount.getEndDate()).thenReturn(new java.util.Date(System.currentTimeMillis() + 2000000));
        when(discount.getPercentage()).thenReturn(new java.math.BigDecimal("0"));
        assertTrue(discountService.validateForProduct("PROMO10", product).isEmpty());
    }

    @Test
    void validateForProductProductoSinDescuento() {
        Product product = mock(Product.class);
        Discount discount = mock(Discount.class);
        when(discountRepo.findByIdDiscount("PROMO10")).thenReturn(Optional.of(discount));
        when(discount.getStartDate()).thenReturn(new java.util.Date(System.currentTimeMillis() - 2000000));
        when(discount.getEndDate()).thenReturn(new java.util.Date(System.currentTimeMillis() + 2000000));
        when(discount.getPercentage()).thenReturn(new java.math.BigDecimal("10"));
        when(product.getDiscount()).thenReturn(null);
        assertTrue(discountService.validateForProduct("PROMO10", product).isEmpty());
    }

    @Test
    void validateForProductCodigoNoCoincideConProducto() {
        Product product = mock(Product.class);
        Discount discount = mock(Discount.class);
        Discount prodDiscount = mock(Discount.class);
        when(discountRepo.findByIdDiscount("PROMO10")).thenReturn(Optional.of(discount));
        when(discount.getStartDate()).thenReturn(new java.util.Date(System.currentTimeMillis() - 2000000));
        when(discount.getEndDate()).thenReturn(new java.util.Date(System.currentTimeMillis() + 2000000));
        when(discount.getPercentage()).thenReturn(new java.math.BigDecimal("10"));
        when(product.getDiscount()).thenReturn(prodDiscount);
        when(prodDiscount.getIdDiscount()).thenReturn("OTROCODE");
        assertTrue(discountService.validateForProduct("PROMO10", product).isEmpty());
    }

    @Test
    void validateForProductValido() {
        Product product = mock(Product.class);
        Discount discount = mock(Discount.class);
        when(discountRepo.findByIdDiscount("PROMO10")).thenReturn(Optional.of(discount));
        when(discount.getStartDate()).thenReturn(new java.util.Date(System.currentTimeMillis() - 2000000));
        when(discount.getEndDate()).thenReturn(new java.util.Date(System.currentTimeMillis() + 2000000));
        when(discount.getPercentage()).thenReturn(new java.math.BigDecimal("10"));
        when(product.getDiscount()).thenReturn(discount);
        when(discount.getIdDiscount()).thenReturn("PROMO10");
        Optional<Discount> result = discountService.validateForProduct("PROMO10", product);
        assertTrue(result.isPresent());
        assertEquals(discount, result.get());
    }

    //tests for applyPct method
    @Test
    void applyPctCalculaCorrectamente() {
        java.math.BigDecimal result = discountService.applyPct(PRECIO, PCT);
        assertEquals(RESULTADO_ESPERADO, result);
    }

    @Test
    void applyPctConNulosRetornaPrecio() {
        assertEquals(PRECIO, discountService.applyPct(PRECIO, null));
        assertNull(discountService.applyPct(null, PCT));
    }
}
