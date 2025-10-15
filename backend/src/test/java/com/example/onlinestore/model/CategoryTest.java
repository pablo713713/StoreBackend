package com.example.onlinestore.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Category model unit tests")
class CategoryTest {

    @Test
    @DisplayName("Constructor JPA sin argumentos debe crear instancia válida")
    void jpaConstructor_shouldCreateInstance() {
        Category category = new Category();
        assertNotNull(category, "El constructor vacío debe crear instancia");
        assertNull(category.getId(), "id debe ser null por defecto");
        assertNull(category.getNameCategory(), "nameCategory debe ser null por defecto");
        assertNull(category.getDescription(), "description debe ser null por defecto");
        assertNotNull(category.getListProducts(), "listProducts debe inicializarse no-null");
        assertTrue(category.getListProducts().isEmpty(), "listProducts debe iniciar vacía");
    }

    @Test
    @DisplayName("Constructor con argumentos debe inicializar campos y lista vacía")
    void argsConstructor_shouldInitializeFields() {
        Category category = new Category("Medicamentos", "Productos farmacéuticos");
        assertEquals("Medicamentos", category.getNameCategory());
        assertEquals("Productos farmacéuticos", category.getDescription());
        assertNull(category.getId(), "id debe ser null hasta persistir");
        assertNotNull(category.getListProducts(), "listProducts debe estar inicializada");
        assertTrue(category.getListProducts().isEmpty(), "listProducts debe iniciar vacía");
    }

    @Test
    @DisplayName("getId por defecto debe ser null")
    void getId_defaultIsNull() {
        Category category = new Category();
        assertNull(category.getId());
    }

    @Test
    @DisplayName("get/setNameCategory deben asignar y retornar correctamente")
    void nameCategory_getterSetter() {
        Category category = new Category();
        assertNull(category.getNameCategory());

        category.setNameCategory("Vitaminas");
        assertEquals("Vitaminas", category.getNameCategory());

        category.setNameCategory(null);
        assertNull(category.getNameCategory());
    }

    @Test
    @DisplayName("get/setDescription deben asignar y retornar correctamente")
    void description_getterSetter() {
        Category category = new Category();
        assertNull(category.getDescription());

        category.setDescription("Venta libre");
        assertEquals("Venta libre", category.getDescription());

        category.setDescription(null);
        assertNull(category.getDescription());
    }

    @Test
    @DisplayName("getListProducts debe devolver lista no-null y modificable")
    void getListProducts_shouldBeNonNullAndModifiable() {
        Category category = new Category();
        List<Product> products = category.getListProducts();

        assertNotNull(products, "La lista no debe ser null");
        assertTrue(products.isEmpty(), "La lista debe iniciar vacía");

        Product p = Mockito.mock(Product.class);
        products.add(p);

        assertEquals(1, products.size(), "La lista debe permitir agregar elementos");
        assertSame(p, products.get(0), "Debe contener la misma referencia agregada");
    }

    @Test
    @DisplayName("setListProducts debe asignar la referencia (incluido null)")
    void setListProducts_shouldAssignReference() {
        Category category = new Category();

        Product p1 = Mockito.mock(Product.class);
        Product p2 = Mockito.mock(Product.class);
        List<Product> newList = new ArrayList<>();
        newList.add(p1);
        newList.add(p2);

        category.setListProducts(newList);
        assertSame(newList, category.getListProducts(), "Debe devolver la misma referencia asignada");
        assertEquals(2, category.getListProducts().size());

        category.setListProducts(null);
        assertNull(category.getListProducts(), "Debe aceptar null sin excepción");
    }
}
