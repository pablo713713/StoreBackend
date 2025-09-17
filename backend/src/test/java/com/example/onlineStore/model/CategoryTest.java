package com.example.onlineStore.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testCategoryCreation() {
        Category category = new Category("Electronics", "Category for electronic devices.");
        assertNotNull(category);
        assertEquals("Electronics", category.getNameCategory());
        assertEquals("Category for electronic devices.", category.getDescription());
        assertNotNull(category.getListProducts());
        assertTrue(category.getListProducts().isEmpty());
    }

    @Test
    void testSetProducts() {
        Category category = new Category("Electronics", "Category for electronic devices.");
        category.setListProducts(new ArrayList<>());
        assertTrue(category.getListProducts().isEmpty());
    }
}