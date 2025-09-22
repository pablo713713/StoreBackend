package com.example.onlineStore.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {
    private static final String ELECTRONICS = "Electronics";
    private static final String ELECTRONICS_DESCRIPTION = "Category for electronic devices.";

    @Test
    void testCategoryCreation() {
        Category category = new Category(ELECTRONICS, ELECTRONICS_DESCRIPTION);
        assertNotNull(category);
        assertEquals(ELECTRONICS, category.getNameCategory());
        assertEquals(ELECTRONICS_DESCRIPTION, category.getDescription());
        assertNotNull(category.getListProducts());
        assertTrue(category.getListProducts().isEmpty());
        assertTrue(category.getListProducts().isEmpty());
    }

    @Test
    void testSetProducts() {
        Category category = new Category(ELECTRONICS, ELECTRONICS_DESCRIPTION);
        category.setListProducts(new ArrayList<>());
        assertTrue(category.getListProducts().isEmpty());
        assertTrue(category.getListProducts().isEmpty());
    }
}