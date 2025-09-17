package com.example.onlineStore.controller;

import com.example.onlineStore.model.Inventory;
import com.example.onlineStore.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Inventory> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Inventory inventory) {
        try {
            Inventory created = service.create(inventory);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/categories")
    public ResponseEntity<?> replaceCategories(
            @PathVariable Long id,
            @RequestBody List<Long> categoryIds) {
        try {
            return ResponseEntity.ok(service.replaceCategories(id, categoryIds));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/categories/{categoryId}")
    public ResponseEntity<?> addCategory(
            @PathVariable Long id,
            @PathVariable Long categoryId) {
        try {
            return ResponseEntity.ok(service.addCategory(id, categoryId));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/categories/{categoryId}")
    public ResponseEntity<?> removeCategory(
            @PathVariable Long id,
            @PathVariable Long categoryId) {
        try {
            return ResponseEntity.ok(service.removeCategory(id, categoryId));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
