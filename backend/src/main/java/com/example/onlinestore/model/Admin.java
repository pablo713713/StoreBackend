package com.example.onlinestore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin extends User {

    @Column(nullable = false, unique = true, length = 50)
    private String accessCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", referencedColumnName = "id", unique = true)
    private Inventory adminInventory;

    protected Admin() {
        // requerido por JPA
    }

    public Admin(String name, String firstLastName, String secondLastName,
                 String accessCode, Inventory adminInventory) {
        super(name, firstLastName, secondLastName);
        this.accessCode = accessCode;
        this.adminInventory = adminInventory;
    }
    /**
     * @deprecated Este constructor está obsoleto desde la versión 1.0
     *             y será eliminado en una futura versión.
     *             Usa {@link #Admin(String, String, String, String, Inventory)} en su lugar.
     */
    @Deprecated(since = "1.0", forRemoval = true)
    public Admin(String name, String firstLastName, String secondLastName,
                 int ignored, String accessCode, Inventory adminInventory) {
        this(name, firstLastName, secondLastName, accessCode, adminInventory);
    }


    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public Inventory getAdminInventory() {
        return adminInventory;
    }

    public void setAdminInventory(Inventory adminInventory) {
        this.adminInventory = adminInventory;
    }
}
