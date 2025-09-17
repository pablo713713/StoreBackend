package com.example.onlineStore.repository;

import com.example.onlineStore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> findByClient_Id(Long clientId);
}
