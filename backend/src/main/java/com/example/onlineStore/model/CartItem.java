package com.example.onlineStore.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id")
    private ShoppingCart cart;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "coupon_pct", precision = 5, scale = 2)
    private BigDecimal couponPct;

    protected CartItem() {
        // requerido por JPA
    }

    public CartItem(ShoppingCart cart, Product product, int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public BigDecimal getCouponPct() {
        return couponPct;
    }

    public void setCouponPct(BigDecimal couponPct) {
        this.couponPct = couponPct;
    }

    public boolean hasCoupon() {
        return couponPct != null && couponPct.compareTo(BigDecimal.ZERO) > 0;
    }
}
