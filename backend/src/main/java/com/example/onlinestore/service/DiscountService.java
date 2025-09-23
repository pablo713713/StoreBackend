package com.example.onlinestore.service;

import com.example.onlinestore.model.Discount;
import com.example.onlinestore.model.Product;

import java.math.BigDecimal;
import java.util.Optional;

public interface DiscountService {

    Optional<Discount> validateForProduct(String code, Product product);

    BigDecimal applyPct(BigDecimal price, BigDecimal pct);
}
