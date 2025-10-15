package com.example.onlinestore.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.onlinestore.model.Product;
import com.example.onlinestore.model.Discount;
import com.example.onlinestore.repository.ProductRepository;
import com.example.onlinestore.repository.DiscountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    // Constantes compartidas para evitar duplicación de literales
    private static final Long PRODUCT_ID = 1L;
    private static final Product PRODUCT = new Product("ID1", "Nombre", "Desc", new java.math.BigDecimal("100.00"), 10);
    
    @Mock
    private ProductRepository productRepository;
    @Mock
    private DiscountRepository discountRepository;
    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        // Preparación inicial para los tests de ProductService
    }

    //test for getProductById method

    @Test
    void getProductByIdDevuelveProductoSiExiste() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(java.util.Optional.of(PRODUCT));
        var result = productService.getProductById(PRODUCT_ID);
        assertTrue(result.isPresent());
        assertEquals(PRODUCT, result.get());
    }

    @Test
    void getProductByIdVacioSiNoExiste() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(java.util.Optional.empty());
        var result = productService.getProductById(PRODUCT_ID);
        assertTrue(result.isEmpty());
    }

    //test for createProduct method

    @Test
    void createProductGuardaYRetornaProducto() {
        when(productRepository.save(PRODUCT)).thenReturn(PRODUCT);
        Product result = productService.createProduct(PRODUCT);
        assertEquals(PRODUCT, result);
        verify(productRepository).save(PRODUCT);
    }

    //test for updateProduct method

    @Test
    void updateProductActualizaYRetornaProducto() {
        Product detalles = new Product("ID1", "NuevoNombre", "NuevaDesc", new java.math.BigDecimal("150.00"), 20);
        when(productRepository.findById(PRODUCT_ID)).thenReturn(java.util.Optional.of(PRODUCT));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = productService.updateProduct(PRODUCT_ID, detalles);
        assertEquals("NuevoNombre", result.getNameProduct());
        assertEquals("NuevaDesc", result.getDescription());
        assertEquals(new java.math.BigDecimal("150.00"), result.getPrice());
        assertEquals(20, result.getStock());
        verify(productRepository).save(result);
    }

    @Test
    void updateProductLanzaExcepcionSiNoExiste() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(java.util.Optional.empty());
        Product detalles = new Product("ID1", "NuevoNombre", "NuevaDesc", new java.math.BigDecimal("150.00"), 20);
        Exception ex = assertThrows(IllegalStateException.class, () ->
            productService.updateProduct(PRODUCT_ID, detalles)
        );
        assertTrue(ex.getMessage().contains("Product not found with id"));
    }

    //test for deleteProduct method

    @Test
    void deleteProductEliminaSiExiste() {
        when(productRepository.existsById(PRODUCT_ID)).thenReturn(true);
        productService.deleteProduct(PRODUCT_ID);
        verify(productRepository).deleteById(PRODUCT_ID);
    }

    @Test
    void deleteProductLanzaExcepcionSiNoExiste() {
        when(productRepository.existsById(PRODUCT_ID)).thenReturn(false);
        Exception ex = assertThrows(IllegalStateException.class, () ->
            productService.deleteProduct(PRODUCT_ID)
        );
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    //test for getAllProducts method

    @Test
    void getAllProductsRetornaLista() {
        java.util.List<Product> productos = java.util.List.of(PRODUCT);
        when(productRepository.findAll()).thenReturn(productos);
        var result = productService.getAllProducts();
        assertEquals(productos, result);
        verify(productRepository).findAll();
    }

    //test for attachDiscount method
    private static final String DISCOUNT_CODE = "PROMO10";
    private static final Discount DISCOUNT = new Discount(DISCOUNT_CODE, "Promo 10%", new java.math.BigDecimal("0.10"), new java.util.Date(), new java.util.Date());

    @Test
    void attachDiscountLanzaExcepcionSiProductoNoExiste() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(java.util.Optional.empty());
        Exception ex = assertThrows(IllegalStateException.class, () ->
            productService.attachDiscount(PRODUCT_ID, DISCOUNT_CODE)
        );
        assertTrue(ex.getMessage().contains("Product not found"));
    }

    @Test
    void attachDiscountLanzaExcepcionSiDiscountNoExiste() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(java.util.Optional.of(PRODUCT));
        when(discountRepository.findByIdDiscount(DISCOUNT_CODE)).thenReturn(java.util.Optional.empty());
        Exception ex = assertThrows(IllegalStateException.class, () ->
            productService.attachDiscount(PRODUCT_ID, DISCOUNT_CODE)
        );
        assertTrue(ex.getMessage().contains("Discount not found"));
    }

    @Test
    void attachDiscountAsignaYGuarda() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(java.util.Optional.of(PRODUCT));
        when(discountRepository.findByIdDiscount(DISCOUNT_CODE)).thenReturn(java.util.Optional.of(DISCOUNT));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = productService.attachDiscount(PRODUCT_ID, DISCOUNT_CODE);
        assertEquals(DISCOUNT, result.getDiscount());
        verify(productRepository).save(PRODUCT);
    }

    //test for removeDiscount method

    @Test
    void removeDiscountEliminaDescuentoSiExiste() {
        Product productoConDescuento = new Product("ID1", "Nombre", "Desc", new java.math.BigDecimal("100.00"), 10);
        productoConDescuento.setDiscount(DISCOUNT);
        when(productRepository.findById(PRODUCT_ID)).thenReturn(java.util.Optional.of(productoConDescuento));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = productService.removeDiscount(PRODUCT_ID);
        assertNull(result.getDiscount());
        verify(productRepository).save(productoConDescuento);
    }

    @Test
    void removeDiscountLanzaExcepcionSiNoExiste() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(java.util.Optional.empty());
        Exception ex = assertThrows(IllegalStateException.class, () ->
            productService.removeDiscount(PRODUCT_ID)
        );
        assertTrue(ex.getMessage().contains("Product not found"));
    }
}
