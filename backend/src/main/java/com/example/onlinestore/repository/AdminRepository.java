package com.example.onlinestore.repository;

import com.example.onlinestore.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByAccessCode(String accessCode);
    Optional<Admin> findByAdminInventory_Id(Long inventoryId);
}
