package com.example.onlinestore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class OnlineStoreApplicationTests {

    @Test
    void contextLoads() {
        // Esta prueba se dejo intencionalmente vacia para verificar que el contexto de la aplicacion Spring se carga correctamente.
        // Ya no se lanza la excepcion, por lo que la prueba pasara si el contexto de Spring Boot se carga sin problemas.
    }
}