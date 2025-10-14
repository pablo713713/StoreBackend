package com.example.onlinestore.controller;

import com.example.onlinestore.dto.ProductDTO;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    private ProductService productService;
    private ProductController productController;

    @BeforeEach
    void setUp() {
        // Mockeamos el ProductService
        productService = mock(ProductService.class);
        productController = new ProductController(productService);
    }

    @Test
    void testGetAllProducts() {
        // Creamos un producto simulado en DTO con BigDecimal
        ProductDTO productDTO = new ProductDTO(1L, "Product A", "image.jpg", "Description", BigDecimal.valueOf(100.0), 50);

        // Simulamos la respuesta del servicio
        when(productService.getAllProducts()).thenReturn(List.of(new Product("1L", "Product A", "Description", BigDecimal.valueOf(100.0), 50)));

        List<ProductDTO> result = productController.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Product A", result.get(0).nameProduct());
        assertEquals(BigDecimal.valueOf(100.0), result.get(0).price());
        verify(productService).getAllProducts();
    }

}
