package com.example.onlineStore.controller;

import com.example.onlineStore.model.Client;
import com.example.onlineStore.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(
        origins = "http://localhost:4200",
        allowCredentials = "true",
        allowedHeaders = {"Content-Type", "Authorization"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/create")
    public Client createClient(@RequestBody Client client) {
        return clientService.saveClient(client);
    }

    @GetMapping("/{clientId}")
    public Client getClient(@PathVariable Long clientId) {
        return clientService.getClientById(clientId);
    }

    @GetMapping("/all")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @DeleteMapping("/delete/{clientId}")
    public String deleteClient(@PathVariable Long clientId) {
        clientService.deleteClient(clientId);
        return "Cliente eliminado correctamente";
    }

    @PostMapping("/login")
    public Client login(@RequestBody Map<String, String> body) {
        return clientService.login(body.get("email"), body.get("password"));
    }
}
