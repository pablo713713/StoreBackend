package com.example.onlineStore.controller;

import com.example.onlineStore.dto.ProductDTO;
import com.example.onlineStore.model.Product;
import com.example.onlineStore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(p -> ResponseEntity.ok(toDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/discount/{code}")
    public ResponseEntity<Product> attachDiscount(@PathVariable Long id, @PathVariable String code) {
        try {
            return ResponseEntity.ok(productService.attachDiscount(id, code));
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/discount")
    public ResponseEntity<Product> removeDiscount(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productService.removeDiscount(id));
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private ProductDTO toDto(Product p) {
        return new ProductDTO(
                p.getId(),
                p.getNameProduct(),
                p.getImageUrl(),
                p.getDescription(),
                p.getPrice(),
                p.getStock()
        );
    }
}
