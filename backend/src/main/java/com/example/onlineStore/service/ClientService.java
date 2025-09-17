package com.example.onlineStore.service;

import com.example.onlineStore.model.Client;
import com.example.onlineStore.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public void deleteClient(Long clientId) {
        clientRepository.deleteById(clientId);
    }

    public Client login(String email, String password) {
        return clientRepository.findByEmail(email)
                .filter(c -> c.getPassword().equals(password)) // en producción usar hash seguro
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));
    }
}
