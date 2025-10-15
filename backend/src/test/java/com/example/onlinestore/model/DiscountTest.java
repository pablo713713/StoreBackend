package com.example.onlinestore.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Discount model unit tests")
class DiscountTest {

    @Test
    @DisplayName("Constructor JPA sin argumentos: instancia válida y campos en null")
    void jpaConstructor_shouldCreateInstance() {
        Discount d = new Discount(); // mismo paquete
        assertNotNull(d);
        assertNull(d.getId());
        assertNull(d.getIdDiscount());
        assertNull(d.getNameDiscount());
        assertNull(d.getPercentage());
        assertNull(d.getStartDate());
        assertNull(d.getEndDate());
    }

    @Test
    @DisplayName("Constructor con argumentos: inicializa todos los campos (id se mantiene null)")
    void argsConstructor_shouldInitializeFields() {
        Date start = new Date(System.currentTimeMillis() - 3600_000); // ahora -1h
        Date end   = new Date(System.currentTimeMillis() + 3600_000); // ahora +1h
        Discount d = new Discount("DISC-10", "Descuento 10%", new BigDecimal("0.10"), start, end);

        assertNull(d.getId());
        assertEquals("DISC-10", d.getIdDiscount());
        assertEquals("Descuento 10%", d.getNameDiscount());
        assertEquals(new BigDecimal("0.10"), d.getPercentage());
        assertEquals(start, d.getStartDate());
        assertEquals(end, d.getEndDate());
    }

    @Test
    @DisplayName("getId: por defecto null; campo público puede setearse para test")
    void getId_defaultAndPublicField() {
        Discount d = new Discount();
        assertNull(d.getId());
        d.id = 99L; // campo es público en el modelo
        assertEquals(99L, d.getId());
        d.id = null;
        assertNull(d.getId());
    }

    @Test
    @DisplayName("get/setIdDiscount")
    void idDiscount_getterSetter() {
        Discount d = new Discount();
        assertNull(d.getIdDiscount());
        d.setIdDiscount("DISC-20");
        assertEquals("DISC-20", d.getIdDiscount());
        d.setIdDiscount(null);
        assertNull(d.getIdDiscount());
    }

    @Test
    @DisplayName("get/setNameDiscount")
    void nameDiscount_getterSetter() {
        Discount d = new Discount();
        assertNull(d.getNameDiscount());
        d.setNameDiscount("Black Friday");
        assertEquals("Black Friday", d.getNameDiscount());
        d.setNameDiscount(null);
        assertNull(d.getNameDiscount());
    }

    @Test
    @DisplayName("get/setPercentage")
    void percentage_getterSetter() {
        Discount d = new Discount();
        assertNull(d.getPercentage());
        d.setPercentage(new BigDecimal("0.25"));
        assertEquals(new BigDecimal("0.25"), d.getPercentage());
        d.setPercentage(null);
        assertNull(d.getPercentage());
    }

    @Test
    @DisplayName("get/setStartDate y get/setEndDate")
    void dates_getterSetter() {
        Discount d = new Discount();
        assertNull(d.getStartDate());
        assertNull(d.getEndDate());

        Date s = new Date(System.currentTimeMillis() - 1000);
        Date e = new Date(System.currentTimeMillis() + 1000);

        d.setStartDate(s);
        d.setEndDate(e);

        assertEquals(s, d.getStartDate());
        assertEquals(e, d.getEndDate());

        d.setStartDate(null);
        d.setEndDate(null);
        assertNull(d.getStartDate());
        assertNull(d.getEndDate());
    }

    // --------- isActive() ---------

    @Test
    @DisplayName("isActive: ahora dentro del rango (start < now < end) → true")
    void isActive_trueWhenNowInsideWindow() {
        Date start = new Date(System.currentTimeMillis() - 3600_000); // -1h
        Date end   = new Date(System.currentTimeMillis() + 3600_000); // +1h
        Discount d = new Discount("D","N", new BigDecimal("0.10"), start, end);
        assertTrue(d.isActive(), "Debe ser true si ahora está estrictamente entre start y end");
    }

    @Test
    @DisplayName("isActive: ahora antes del inicio (now < start) → false")
    void isActive_falseWhenBeforeStart() {
        Date start = new Date(System.currentTimeMillis() + 3600_000); // +1h
        Date end   = new Date(System.currentTimeMillis() + 7200_000); // +2h
        Discount d = new Discount("D","N", new BigDecimal("0.10"), start, end);
        assertFalse(d.isActive(), "Debe ser false si ahora está antes de start");
    }

    @Test
    @DisplayName("isActive: ahora después del fin (now > end) → false")
    void isActive_falseWhenAfterEnd() {
        Date start = new Date(System.currentTimeMillis() - 7200_000); // -2h
        Date end   = new Date(System.currentTimeMillis() - 3600_000); // -1h
        Discount d = new Discount("D","N", new BigDecimal("0.10"), start, end);
        assertFalse(d.isActive(), "Debe ser false si ahora está después de end");
    }

    @Test
    @DisplayName("isActive: ventana degenerada (start == end en el futuro) → false")
    void isActive_falseWhenStartEqualsEndFuture() {
        long t = System.currentTimeMillis() + 3600_000; // +1h
        Date same = new Date(t);
        Discount d = new Discount("D","N", new BigDecimal("0.10"), same, same);
        assertFalse(d.isActive(), "start==end no puede contener un now estricto entre ambos");
    }

    @Test
    @DisplayName("isActive: startDate null provoca NullPointerException")
    void isActive_startDateNull_throwsNpe() {
        Discount d = new Discount("D", "N", new BigDecimal("0.10"), null, new Date());
        assertThrows(NullPointerException.class, d::isActive, "startDate null → NPE esperada");
    }

    @Test
    @DisplayName("isActive: endDate null provoca NullPointerException (se evalúa segunda condición)")
    void isActive_endDateNull_throwsNpe() {
        // Colocamos start en el pasado para que la primera condición sea TRUE
        Date start = new Date(System.currentTimeMillis() - 3600_000);
        Discount d = new Discount("D", "N", new BigDecimal("0.10"), start, null);
        assertThrows(NullPointerException.class, d::isActive, "endDate null → NPE esperada");
    }
}
