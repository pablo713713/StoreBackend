package com.example.onlinestore.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("ShoppingCart model unit tests")
class ShoppingCartTest {

    @Test
    @DisplayName("Constructor JPA sin args: id y client null; items set inicializado vacío")
    void jpaCtor_shouldInitEmptySet() {
        ShoppingCart cart = new ShoppingCart(); // mismo package => permitido
        assertNotNull(cart);
        assertNull(cart.getId());
        assertNull(cart.getClient());
        assertNotNull(cart.getItemsSet());
        assertTrue(cart.getItemsSet().isEmpty());
    }

    @Test
    @DisplayName("Constructor con Client: asigna client y deja items set vacío")
    void ctorWithClient_shouldAssignClient() {
        Client client = mock(Client.class);
        ShoppingCart cart = new ShoppingCart(client);

        assertSame(client, cart.getClient());
        assertNotNull(cart.getItemsSet());
        assertTrue(cart.getItemsSet().isEmpty());
        assertNull(cart.getId());
    }

    @Test
    @DisplayName("create(): devuelve instancia con items set vacío")
    void create_shouldReturnInstance() {
        ShoppingCart cart = ShoppingCart.create();
        assertNotNull(cart);
        assertNotNull(cart.getItemsSet());
        assertTrue(cart.getItemsSet().isEmpty());
    }

    @Test
    @DisplayName("get/setClient: asigna y retorna la misma referencia (incluye null)")
    void client_getterSetter() {
        ShoppingCart cart = new ShoppingCart();
        assertNull(cart.getClient());

        Client c = mock(Client.class);
        cart.setClient(c);
        assertSame(c, cart.getClient());

        cart.setClient(null);
        assertNull(cart.getClient());
    }

    @Test
    @DisplayName("getItemsSet: no-null por defecto; setItemsSet acepta set o null")
    void itemsSet_getterSetter() {
        ShoppingCart cart = new ShoppingCart();
        assertNotNull(cart.getItemsSet());
        assertTrue(cart.getItemsSet().isEmpty());

        Set<CartItem> newSet = new HashSet<>();
        cart.setItemsSet(newSet);
        assertSame(newSet, cart.getItemsSet());

        cart.setItemsSet(null);
        assertNull(cart.getItemsSet());
    }

    @Test
    @DisplayName("getItems(): mapea Product->quantity desde el set interno")
    void getItems_buildsMapFromSet() {
        ShoppingCart cart = new ShoppingCart();
        Product p1 = mock(Product.class);
        Product p2 = mock(Product.class);

        // Poblamos el set interno con dos items
        cart.getItemsSet().add(new CartItem(cart, p1, 2));
        cart.getItemsSet().add(new CartItem(cart, p2, 5));

        Map<Product, Integer> map = cart.getItems();
        assertNotNull(map);
        assertEquals(2, map.size());
        assertEquals(2, map.get(p1));
        assertEquals(5, map.get(p2));
    }

    @Test
    @DisplayName("setItems(null) limpia; setItems filtra entradas inválidas")
    void setItems_clearsAndFilters() {
        ShoppingCart cart = new ShoppingCart();
        // Pre-cargado
        Product old = mock(Product.class);
        cart.getItemsSet().add(new CartItem(cart, old, 9));

        // 1) setItems(null) => limpia
        cart.setItems(null);
        assertTrue(cart.getItemsSet().isEmpty(), "Debe limpiar cuando map es null");

        // 2) setItems con válidas e inválidas
        Product p1 = mock(Product.class);
        Product p2 = mock(Product.class);

        Map<Product, Integer> map = new HashMap<>();
        map.put(p1, 3);          // válida
        map.put(p2, 0);          // inválida (qty <= 0)
        map.put(null, 4);        // inválida (key null)
        // value null → inválida
        map.put(mock(Product.class), null);

        cart.setItems(map);

        // Solo debe existir p1 con qty=3
        assertEquals(1, cart.getItemsSet().size());
        CartItem only = cart.getItemsSet().iterator().next();
        assertSame(p1, only.getProduct());
        assertEquals(3, only.getQuantity());
    }

    @Test
    @DisplayName("addProduct: agrega nuevo item si no existe; si existe, suma cantidades")
    void addProduct_behaviour() {
        ShoppingCart cart = new ShoppingCart();
        Product p = mock(Product.class);

        // No existe -> crea nuevo con qty
        cart.addProduct(p, 2);
        assertEquals(1, cart.getItemsSet().size());
        CartItem item = cart.getItemsSet().iterator().next();
        assertSame(p, item.getProduct());
        assertEquals(2, item.getQuantity());

        // Ya existe -> suma
        cart.addProduct(p, 3);
        assertEquals(1, cart.getItemsSet().size(), "No debe crear un nuevo item para el mismo producto");
        CartItem same = cart.getItemsSet().iterator().next();
        assertEquals(5, same.getQuantity(), "Debe sumar 2 + 3 = 5");
    }

    @Test
    @DisplayName("removeProduct: elimina todas las ocurrencias del producto (si existe)")
    void removeProduct_shouldRemoveAllMatches() {
        ShoppingCart cart = new ShoppingCart();
        Product p1 = mock(Product.class);
        Product p2 = mock(Product.class);

        cart.addProduct(p1, 1);
        cart.addProduct(p2, 2);

        cart.removeProduct(p1);
        assertEquals(1, cart.getItemsSet().size());
        CartItem remaining = cart.getItemsSet().iterator().next();
        assertSame(p2, remaining.getProduct());

        // Remover inexistente no debe fallar
        Product notPresent = mock(Product.class);
        assertDoesNotThrow(() -> cart.removeProduct(notPresent));
        assertEquals(1, cart.getItemsSet().size());
    }

    @Test
    @DisplayName("updateProduct: si existe y qty>0, actualiza; si qty<=0, elimina; si no existe, no cambia")
    void updateProduct_behaviour() {
        ShoppingCart cart = new ShoppingCart();
        Product p1 = mock(Product.class);
        Product p2 = mock(Product.class);

        cart.addProduct(p1, 2);

        // Caso existe y >0
        cart.updateProduct(p1, 7);
        assertEquals(1, cart.getItemsSet().size());
        CartItem updated = cart.getItemsSet().iterator().next();
        assertSame(p1, updated.getProduct());
        assertEquals(7, updated.getQuantity());

        // Caso existe y <=0 => elimina
        cart.updateProduct(p1, 0);
        assertTrue(cart.getItemsSet().isEmpty(), "Debe eliminar el item cuando qty <= 0");

        // Caso no existe => no cambia
        cart.addProduct(p1, 3);
        int sizeBefore = cart.getItemsSet().size();
        cart.updateProduct(p2, 10); // p2 no está
        assertEquals(sizeBefore, cart.getItemsSet().size(), "No debe cambiar al no encontrar el producto");
    }

    @Test
    @DisplayName("calculateTotal: set vacío -> 0; con items -> suma de priceWithDiscount * qty")
    void calculateTotal_shouldSumCorrectly() {
        ShoppingCart cart = new ShoppingCart();

        // Vacío -> 0
        assertEquals(0, cart.calculateTotal().compareTo(BigDecimal.ZERO));

        // Preparamos productos con diferentes precios/desc
        Product p1 = mock(Product.class);
        Product p2 = mock(Product.class);

        when(p1.getPriceWithDiscount()).thenReturn(new BigDecimal("10.50"));
        when(p2.getPriceWithDiscount()).thenReturn(new BigDecimal("3.00"));

        cart.addProduct(p1, 3); // 3 * 10.50 = 31.50
        cart.addProduct(p2, 4); // 4 *  3.00 = 12.00  => total = 43.50

        BigDecimal total = cart.calculateTotal();
        assertEquals(0, total.compareTo(new BigDecimal("43.50")));
    }
}
