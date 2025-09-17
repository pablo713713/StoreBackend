package com.example.onlineStore.controller;

import com.example.onlineStore.model.Admin;
import com.example.onlineStore.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    @GetMapping
    public List<Admin> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Admin admin) {
        try {
            Admin created = service.create(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalStateException e) {
            return buildConflictOrBadRequest(e, "Invalid admin data");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Admin details) {
        try {
            return ResponseEntity.ok(service.update(id, details));
        } catch (IllegalStateException e) {
            String msg = messageOrDefault(e, "Update failed");
            HttpStatus status = isConflictMessage(msg) ? HttpStatus.CONFLICT : HttpStatus.NOT_FOUND;
            return ResponseEntity.status(status).body(msg);
        }
    }

    @PutMapping("/{id}/inventory/{inventoryId}")
    public ResponseEntity<?> setInventory(@PathVariable Long id, @PathVariable Long inventoryId) {
        try {
            return ResponseEntity.ok(service.setInventory(id, inventoryId));
        } catch (IllegalStateException e) {
            String msg = messageOrDefault(e, "Inventory set failed");
            HttpStatus status = msg.contains("already assigned") ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(msg);
        }
    }

    @DeleteMapping("/{id}/inventory")
    public ResponseEntity<?> removeInventory(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.removeInventory(id));
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
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

    // ---------------------
    // Helpers privados
    // ---------------------

    private ResponseEntity<?> buildConflictOrBadRequest(IllegalStateException e, String defaultMsg) {
        String msg = messageOrDefault(e, defaultMsg);
        HttpStatus status = isConflictMessage(msg) ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }

    private boolean isConflictMessage(String msg) {
        if (msg == null) return false;
        String lower = msg.toLowerCase();
        return lower.contains("already exists") || lower.contains("already assigned");
        // Añade aquí otros indicadores de conflicto si tu servicio los usa.
    }

    private String messageOrDefault(IllegalStateException e, String fallback) {
        return (e.getMessage() != null && !e.getMessage().isBlank()) ? e.getMessage() : fallback;
    }
}
