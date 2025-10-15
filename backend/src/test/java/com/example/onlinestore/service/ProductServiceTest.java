package com.example.onlinestore.service;

import com.example.onlinestore.model.Product;
import com.example.onlinestore.model.Discount;
import com.example.onlinestore.repository.ProductRepository;
import com.example.onlinestore.repository.DiscountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
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
}
