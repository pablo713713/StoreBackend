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
    @Test
    void testGetProductById() {
        // Creamos un producto simulado
        Product product = new Product("1L", "Product A", "Description", BigDecimal.valueOf(100.0), 50);

        // Simulamos la respuesta del servicio
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        ResponseEntity<ProductDTO> response = productController.getProductById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Product A", response.getBody().nameProduct());
        verify(productService).getProductById(1L);
    }

    @Test
    void testGetProductByIdNotFound() {
        // Simulamos que el producto no existe
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        ResponseEntity<ProductDTO> response = productController.getProductById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productService).getProductById(1L);
    }
    @Test
    void testCreateProduct() {
        // Creamos un producto simulado
        Product product = new Product("1L", "Product A", "Description", BigDecimal.valueOf(100.0), 50);

        // Simulamos la respuesta del servicio
        when(productService.createProduct(any(Product.class))).thenReturn(product);

        // Creamos la solicitud
        ResponseEntity<Product> response = productController.createProduct(product);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Product A", response.getBody().getNameProduct());
        verify(productService).createProduct(any(Product.class));
    }
    @Test
    void testUpdateProduct() {
        // Creamos un producto simulado
        Product existingProduct = new Product("1L", "Product A", "Description", BigDecimal.valueOf(100.0), 50);
        Product updatedProduct = new Product("1L", "Updated Product", "Updated Description", BigDecimal.valueOf(150.0), 60);

        // Simulamos la respuesta del servicio
        when(productService.updateProduct(1L, updatedProduct)).thenReturn(updatedProduct);

        // Realizamos la solicitud
        ResponseEntity<Product> response = productController.updateProduct(1L, updatedProduct);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Product", response.getBody().getNameProduct());
        assertEquals(BigDecimal.valueOf(150.0), response.getBody().getPrice());
        verify(productService).updateProduct(1L, updatedProduct);
    }
    @Test
    void testUpdateProductNotFound() {
        // Creamos un producto simulado
        Product updatedProduct = new Product("1L", "Updated Product", "Updated Description", BigDecimal.valueOf(150.0), 60);

        // Simulamos que el producto no existe
        when(productService.updateProduct(1L, updatedProduct)).thenThrow(new IllegalStateException("Product not found with id: 1L"));

        // Realizamos la solicitud
        ResponseEntity<Product> response = productController.updateProduct(1L, updatedProduct);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productService).updateProduct(1L, updatedProduct);
    }

    @Test
    void testDeleteProduct() {
        // Simulamos la existencia del producto
        doNothing().when(productService).deleteProduct(1L);

        // Realizamos la solicitud
        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService).deleteProduct(1L);
    }

    @Test
    void testDeleteProductNotFound() {
        // Simulamos que el producto no existe
        doThrow(new IllegalStateException("Product not found")).when(productService).deleteProduct(1L);

        // Realizamos la solicitud
        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productService).deleteProduct(1L);
    }
}
