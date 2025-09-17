package com.example.onlineStore.Entity;

import jakarta.persistence.*;
import java.time.Instant;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        Instant now = now();
        createdAt = now; updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = now();
    }

    // Punto de extensión (OCP/DIP): quién provee el "ahora".
    protected Instant now() { return Instant.now(); }

    public Long getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
