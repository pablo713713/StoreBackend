package com.example.onlineStore.dto;

import java.math.BigDecimal;

public class CartItemDTO {

    private Long productId;
    private String nameProduct;
    private BigDecimal price;
    private int qty;

    public CartItemDTO(Long productId, String nameProduct, BigDecimal price, int qty) {
        this.productId = productId;
        this.nameProduct = nameProduct;
        this.price = price;
        this.qty = qty;
    }

    public Long getProductId() { return productId; }
    public String getNameProduct() { return nameProduct; }
    public BigDecimal getPrice() { return price; }
    public int getQty() { return qty; }
}
