package com.example.onlineStore.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    @Test
    void testInventoryCreation() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Electronics", "Electronic gadgets"));
        categories.add(new Category("Books", "Printed and digital books"));

        Inventory inventory = new Inventory(categories);

        assertNotNull(inventory);
        assertNotNull(inventory.getListCategories());
        assertEquals(2, inventory.getListCategories().size());
        assertNotNull(inventory.getLastUpdate());
    }

    @Test
    void testAddAndRemoveCategory() {
        Inventory inventory = new Inventory(new ArrayList<>());
        Category category1 = new Category("Electronics", "Electronic gadgets");
        Category category2 = new Category("Books", "Printed and digital books");

        inventory.addCategory(category1);
        assertEquals(1, inventory.getListCategories().size());

        inventory.addCategory(category2);
        assertEquals(2, inventory.getListCategories().size());

        inventory.removeCategory(category1);
        assertEquals(1, inventory.getListCategories().size());
    }
}