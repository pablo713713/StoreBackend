package com.example.onlinestore.repository;

import com.example.onlinestore.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByIdDiscount(String idDiscount);
}
