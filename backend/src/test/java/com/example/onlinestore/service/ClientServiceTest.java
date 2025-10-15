package com.example.onlinestore.service;

import com.example.onlinestore.repository.ClientRepository;
import com.example.onlinestore.model.Client;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        // Preparar mocks y datos comunes
    }

    //test for getClientById method

    private static final Client CLIENT = new Client(
        "Juan", "Pérez", "García", "C001", "juan@email.com", "pass123");

    @Test
    void getClientByIdDevuelveClienteSiExiste() {
        when(clientRepository.findById(1L)).thenReturn(java.util.Optional.of(CLIENT));
        Client result = clientService.getClientById(1L);
        assertEquals(CLIENT, result);
        verify(clientRepository).findById(1L);
    }

    @Test
    void getClientByIdLanzaExcepcionSiNoExiste() {
        when(clientRepository.findById(2L)).thenReturn(java.util.Optional.empty());
        assertThrows(IllegalArgumentException.class, () ->
            clientService.getClientById(2L)
        );
        verify(clientRepository).findById(2L);
    }
}
