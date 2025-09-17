package com.example.onlineStore.service;

import com.example.onlineStore.model.Discount;
import com.example.onlineStore.model.Product;
import com.example.onlineStore.repository.DiscountRepository;
import com.example.onlineStore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    public ProductService(ProductRepository productRepository,
                          DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product not found with id: " + id));

        product.setNameProduct(productDetails.getNameProduct());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        product.setImageUrl(productDetails.getImageUrl());
        product.setDiscount(productDetails.getDiscount());

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        boolean exists = productRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Product with id " + id + " does not exist");
        }
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Product attachDiscount(Long productId, String code) {
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Product not found: " + productId));
        Discount d = discountRepository.findByIdDiscount(code)
                .orElseThrow(() -> new IllegalStateException("Discount not found: " + code));
        p.setDiscount(d);
        return productRepository.save(p);
    }

    @Transactional
    public Product removeDiscount(Long productId) {
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Product not found: " + productId));
        p.setDiscount(null);
        return productRepository.save(p);
    }
}
