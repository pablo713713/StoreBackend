package com.example.onlineStore.service;

import com.example.onlineStore.model.Admin;
import com.example.onlineStore.model.Inventory;
import com.example.onlineStore.repository.AdminRepository;
import com.example.onlineStore.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final InventoryRepository inventoryRepository;

    public AdminService(AdminRepository adminRepository, InventoryRepository inventoryRepository) {
        this.adminRepository = adminRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    public Admin getById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Admin not found with id: " + id));
    }

    public Admin create(Admin admin) {
        adminRepository.findByAccessCode(admin.getAccessCode())
                .ifPresent(a -> {
                    throw new IllegalStateException("Access code already exists: " + a.getAccessCode());
                });

        if (admin.getAdminInventory() != null && admin.getAdminInventory().getId() != null) {
            Long invId = admin.getAdminInventory().getId();
            Inventory inv = inventoryRepository.findById(invId)
                    .orElseThrow(() -> new IllegalStateException("Inventory not found with id: " + invId));

            adminRepository.findByAdminInventory_Id(invId)
                    .ifPresent(a -> {
                        throw new IllegalStateException("Inventory already assigned to admin id: " + a.getId());
                    });

            admin.setAdminInventory(inv);
        } else {
            admin.setAdminInventory(null);
        }

        return adminRepository.save(admin);
    }

    public Admin update(Long id, Admin details) {
        Admin admin = getById(id);

        if (!admin.getAccessCode().equals(details.getAccessCode())) {
            adminRepository.findByAccessCode(details.getAccessCode())
                    .ifPresent(a -> {
                        throw new IllegalStateException("Access code already exists: " + a.getAccessCode());
                    });
            admin.setAccessCode(details.getAccessCode());
        }

        admin.setName(details.getName());
        admin.setFirstLastName(details.getFirstLastName());
        admin.setSecondLastName(details.getSecondLastName());

        if (details.getAdminInventory() != null && details.getAdminInventory().getId() != null) {
            Long newInvId = details.getAdminInventory().getId();

            if (admin.getAdminInventory() == null || !admin.getAdminInventory().getId().equals(newInvId)) {
                Inventory inv = inventoryRepository.findById(newInvId)
                        .orElseThrow(() -> new IllegalStateException("Inventory not found with id: " + newInvId));

                adminRepository.findByAdminInventory_Id(newInvId)
                        .ifPresent(a -> {
                            if (!a.getId().equals(admin.getId())) {
                                throw new IllegalStateException("Inventory already assigned to admin id: " + a.getId());
                            }
                        });

                admin.setAdminInventory(inv);
            }
        } else {
            admin.setAdminInventory(null);
        }

        return adminRepository.save(admin);
    }

    public void delete(Long id) {
        Admin admin = getById(id);
        adminRepository.delete(admin);
    }

    public Admin setInventory(Long adminId, Long inventoryId) {
        Admin admin = getById(adminId);
        Inventory inv = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new IllegalStateException("Inventory not found with id: " + inventoryId));

        adminRepository.findByAdminInventory_Id(inventoryId)
                .ifPresent(a -> {
                    if (!a.getId().equals(adminId)) {
                        throw new IllegalStateException("Inventory already assigned to admin id: " + a.getId());
                    }
                });

        admin.setAdminInventory(inv);
        return adminRepository.save(admin);
    }

    public Admin removeInventory(Long adminId) {
        Admin admin = getById(adminId);
        admin.setAdminInventory(null);
        return adminRepository.save(admin);
    }
}
