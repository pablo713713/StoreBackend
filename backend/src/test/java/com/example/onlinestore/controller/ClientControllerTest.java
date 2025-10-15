package com.example.onlinestore.controller;

import com.example.onlinestore.model.Client;
import com.example.onlinestore.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ClientControllerTest {

    private ClientService clientService;
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        clientService = mock(ClientService.class);
        clientController = new ClientController(clientService);
    }

    // ===================== CREATE CLIENT =========================
    @Test
    void testCreateClient() {
        Client client = new Client("John", "Doe", "Smith", "12345", "john.doe@example.com", "password123");
        when(clientService.saveClient(any(Client.class))).thenReturn(client);

        Client result = clientController.createClient(client);

        assertNotNull(result);
        assertEquals(client.getEmail(), result.getEmail());
        verify(clientService).saveClient(client);
    }

    // ===================== GET CLIENT ============================
    @Test
    void testGetClient() {
        Client client = new Client("John", "Doe", "Smith", "12345", "john.doe@example.com", "password123");
        when(clientService.getClientById(1L)).thenReturn(client);

        Client result = clientController.getClient(1L);

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        verify(clientService).getClientById(1L);
    }

    @Test
    void testGetClientNotFound() {
        when(clientService.getClientById(1L)).thenThrow(new IllegalArgumentException("Cliente no encontrado"));

        assertThrows(IllegalArgumentException.class, () -> clientController.getClient(1L));
        verify(clientService).getClientById(1L);
    }

    // ===================== GET ALL CLIENTS =======================
    @Test
    void testGetAllClients() {
        Client client1 = new Client("John", "Doe", "Smith", "12345", "john.doe@example.com", "password123");
        Client client2 = new Client("Jane", "Doe", "Smith", "12346", "jane.doe@example.com", "password123");
        when(clientService.getAllClients()).thenReturn(List.of(client1, client2));

        List<Client> result = clientController.getAllClients();

        assertEquals(2, result.size());
        verify(clientService).getAllClients();
    }

    // ===================== DELETE CLIENT =========================
    @Test
    void testDeleteClient() {
        doNothing().when(clientService).deleteClient(1L);

        String result = clientController.deleteClient(1L);

        assertEquals("Cliente eliminado correctamente", result);
        verify(clientService).deleteClient(1L);
    }

    @Test
    void testDeleteClientNotFound() {
        doThrow(new IllegalArgumentException("Cliente no encontrado")).when(clientService).deleteClient(1L);

        assertThrows(IllegalArgumentException.class, () -> clientController.deleteClient(1L));
        verify(clientService).deleteClient(1L);
    }

    // ===================== LOGIN ================================
    @Test
    void testLoginSuccess() {
        Client client = new Client("John", "Doe", "Smith", "12345", "john.doe@example.com", "password123");
        Map<String, String> loginRequest = Map.of("email", "john.doe@example.com", "password", "password123");
        when(clientService.login("john.doe@example.com", "password123")).thenReturn(client);

        Client result = clientController.login(loginRequest);

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        verify(clientService).login("john.doe@example.com", "password123");
    }

    @Test
    void testLoginFailure() {
        Map<String, String> loginRequest = Map.of("email", "john.doe@example.com", "password", "wrongPassword");
        when(clientService.login("john.doe@example.com", "wrongPassword"))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        assertThrows(ResponseStatusException.class, () -> clientController.login(loginRequest));
        verify(clientService).login("john.doe@example.com", "wrongPassword");
    }
}
