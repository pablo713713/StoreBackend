package com.example.onlineStore.controller;

import com.example.onlineStore.model.Category;
import com.example.onlineStore.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Category> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Category category) {
        try {
            Category created = service.create(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Category details) {
        try {
            return ResponseEntity.ok(service.update(id, details));
        } catch (IllegalStateException e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
            }
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("assigned products")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
            }
            return ResponseEntity.notFound().build();
        }
    }
}
