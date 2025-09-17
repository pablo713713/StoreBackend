package com.example.onlineStore.service;

import com.example.onlineStore.model.Discount;
import com.example.onlineStore.model.Product;

import java.math.BigDecimal;
import java.util.Optional;

public interface DiscountService {

    Optional<Discount> validateForProduct(String code, Product product);

    BigDecimal applyPct(BigDecimal price, BigDecimal pct);
}
