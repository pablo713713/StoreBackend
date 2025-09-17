package com.example.onlineStore.service;

import com.example.onlineStore.model.Discount;
import com.example.onlineStore.model.Product;
import com.example.onlineStore.model.Validation;
import com.example.onlineStore.repository.DiscountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository repo;

    public DiscountServiceImpl(DiscountRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<Discount> validateForProduct(String code, Product product) {
        if (code == null || code.isBlank()) return Optional.empty();

        Optional<Discount> od = repo.findByIdDiscount(code.trim());
        if (od.isEmpty()) return Optional.empty();

        Discount d = od.get();

        if (!Validation.hasStarted(d.getStartDate())) return Optional.empty();
        if (!Validation.notExpired(d.getEndDate())) return Optional.empty();
        if (!Validation.isValidPercentage(d.getPercentage())) return Optional.empty();

        if (product.getDiscount() == null) return Optional.empty();
        if (!code.equalsIgnoreCase(product.getDiscount().getIdDiscount())) return Optional.empty();

        return Optional.of(d);
    }

    @Override
    public BigDecimal applyPct(BigDecimal price, BigDecimal pct) {
        if (price == null || pct == null) return price;
        BigDecimal factor = BigDecimal.ONE.subtract(
                pct.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)
        );
        return price.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }
}
