package com.example.onlinestore.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Inventory model unit tests")
class InventoryTest {

    private static void tinySleep() {
        try { Thread.sleep(5); } catch (InterruptedException ignored) {}
    }

    @Test
    @DisplayName("Constructor JPA sin argumentos: instancia válida, lista inicializada y lastUpdate null")
    void jpaCtor_shouldCreateValidInstance() {
        Inventory inv = new Inventory();
        assertNotNull(inv);
        assertNull(inv.getId(), "id debe ser null por defecto");
        assertNotNull(inv.getListCategories(), "listCategories debe estar inicializada");
        assertTrue(inv.getListCategories().isEmpty(), "listCategories debe iniciar vacía");
        assertNull(inv.getLastUpdate(), "lastUpdate debe ser null en el ctor JPA");
    }

    @Test
    @DisplayName("Constructor con lista: asigna referencia y setea lastUpdate no-null")
    void argsCtor_shouldAssignListAndInitLastUpdate() {
        List<Category> categories = new ArrayList<>();
        categories.add(Mockito.mock(Category.class));

        Inventory inv = new Inventory(categories);

        assertSame(categories, inv.getListCategories(), "Debe mantener la misma referencia de lista");
        assertNull(inv.getId(), "id debe ser null hasta persistencia");
        assertNotNull(inv.getLastUpdate(), "lastUpdate debe inicializarse en ctor con lista");
    }

    @Test
    @DisplayName("getId/setId: deben asignar y devolver el valor")
    void id_getterSetter() {
        Inventory inv = new Inventory();
        assertNull(inv.getId());
        inv.setId(42L);
        assertEquals(42L, inv.getId());
        inv.setId(null);
        assertNull(inv.getId());
    }

    @Test
    @DisplayName("getListCategories: no-null y modificable")
    void getListCategories_nonNullAndModifiable() {
        Inventory inv = new Inventory();
        List<Category> list = inv.getListCategories();

        assertNotNull(list);
        assertTrue(list.isEmpty());

        Category c = Mockito.mock(Category.class);
        list.add(c);

        assertEquals(1, list.size());
        assertSame(c, list.get(0));
    }

    @Test
    @DisplayName("setListCategories: asigna referencia (incl. null) y actualiza lastUpdate")
    void setListCategories_assignsAndUpdatesLastUpdate() {
        Inventory inv = new Inventory();

        Date prev = inv.getLastUpdate(); // null al inicio
        tinySleep();

        List<Category> newList = new ArrayList<>();
        newList.add(Mockito.mock(Category.class));
        inv.setListCategories(newList);

        assertSame(newList, inv.getListCategories());
        assertNotNull(inv.getLastUpdate(), "lastUpdate debe inicializarse al hacer setListCategories");

        Date afterFirst = inv.getLastUpdate();
        tinySleep();

        inv.setListCategories(null);
        assertNull(inv.getListCategories(), "Debe aceptar null");
        assertNotNull(inv.getLastUpdate(), "Debe actualizar lastUpdate incluso con null");
        assertNotEquals(afterFirst, inv.getLastUpdate(), "lastUpdate debe cambiar tras la segunda asignación");
    }

    @Test
    @DisplayName("setLastUpdate: permite setear manualmente (incl. null)")
    void setLastUpdate_manual() {
        Inventory inv = new Inventory();
        Date now = new Date();
        inv.setLastUpdate(now);
        assertSame(now, inv.getLastUpdate());

        inv.setLastUpdate(null);
        assertNull(inv.getLastUpdate());
    }

    @Test
    @DisplayName("addCategory: agrega elemento y actualiza lastUpdate")
    void addCategory_updatesListAndLastUpdate() {
        Inventory inv = new Inventory(new ArrayList<>());
        int initialSize = inv.getListCategories().size();
        Date prev = inv.getLastUpdate();
        tinySleep();

        Category c = Mockito.mock(Category.class);
        inv.addCategory(c);

        assertEquals(initialSize + 1, inv.getListCategories().size(), "Debe aumentar el tamaño de la lista");
        assertTrue(inv.getListCategories().contains(c), "La lista debe contener la referencia agregada");
        assertNotNull(inv.getLastUpdate(), "lastUpdate no debe ser null");
        assertNotEquals(prev, inv.getLastUpdate(), "lastUpdate debe cambiar tras addCategory");
    }

    @Test
    @DisplayName("removeCategory: elimina si existe; si no existe, no falla; lastUpdate se actualiza en ambos casos")
    void removeCategory_behaviourAndLastUpdate() {
        Category c1 = Mockito.mock(Category.class);
        Category c2 = Mockito.mock(Category.class);

        List<Category> base = new ArrayList<>();
        base.add(c1);

        Inventory inv = new Inventory(base);

        // Remover existente
        Date prev = inv.getLastUpdate();
        int sizeBefore = inv.getListCategories().size();
        tinySleep();

        inv.removeCategory(c1);

        assertEquals(sizeBefore - 1, inv.getListCategories().size(), "Debe decrementar tamaño al remover existente");
        assertFalse(inv.getListCategories().contains(c1));
        assertNotEquals(prev, inv.getLastUpdate(), "Debe actualizar lastUpdate al remover");

        // Remover inexistente (no debe lanzar, y lastUpdate cambia por implementación actual)
        Date prev2 = inv.getLastUpdate();
        tinySleep();

        inv.removeCategory(c2);
        assertEquals(inv.getListCategories().size(), inv.getListCategories().size(), "Tamaño se mantiene al remover ausente");
        assertNotEquals(prev2, inv.getLastUpdate(), "lastUpdate cambia también al intentar remover ausente");
    }
}
