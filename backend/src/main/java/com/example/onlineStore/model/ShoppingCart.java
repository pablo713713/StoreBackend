package com.example.onlineStore.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", unique = true)
    private Client client;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();

    protected ShoppingCart() {
        // requerido por JPA
    }

    public ShoppingCart(Client client) {
        this.client = client;
    }

    public static ShoppingCart create() {
        return new ShoppingCart();
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<CartItem> getItemsSet() {
        return items;
    }

    public void setItemsSet(Set<CartItem> items) {
        this.items = items;
    }

    @Transient
    public Map<Product, Integer> getItems() {
        Map<Product, Integer> map = new HashMap<>();
        for (CartItem ci : items) {
            map.put(ci.getProduct(), ci.getQuantity());
        }
        return map;
    }

    public void setItems(Map<Product, Integer> map) {
        items.clear();
        if (map != null) {
            for (Map.Entry<Product, Integer> e : map.entrySet()) {
                if (e.getKey() != null && e.getValue() != null && e.getValue() > 0) {
                    items.add(new CartItem(this, e.getKey(), e.getValue()));
                }
            }
        }
    }

    public void addProduct(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().equals(product)) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(this, product, quantity));
    }

    public void removeProduct(Product product) {
        items.removeIf(item -> item.getProduct().equals(product));
    }

    public void updateProduct(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().equals(product)) {
                if (quantity > 0) {
                    item.setQuantity(quantity);
                } else {
                    items.remove(item);
                }
                break;
            }
        }
    }

    public BigDecimal calculateTotal() {
        return items.stream()
                .map(item -> item.getProduct().getPriceWithDiscount()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
