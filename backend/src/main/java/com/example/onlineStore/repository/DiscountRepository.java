package com.example.onlineStore.repository;

import com.example.onlineStore.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByIdDiscount(String idDiscount);
}
