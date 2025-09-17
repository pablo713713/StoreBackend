package com.example.onlineStore.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {
    @Test
    void testAdminCreation() {
        Inventory inventory = new Inventory();
        Admin admin = new Admin("Julian","Pantoja","Suarez",1,"JPS01",inventory);
        assertNotNull(admin);
    }
}