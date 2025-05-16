package com.quotationsystem.controller;

import com.quotationsystem.entity.Client;
import com.quotationsystem.service.implementation.ClientServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("clients")
public class ClientController {

  @Autowired private ClientServiceImpl clientService;

  @PostMapping
  public Client create(@RequestBody Client client) {
    return clientService.create(client);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Client> get(@PathVariable Long id) {
    return clientService
        .get(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public List<Client> getAll() {
    return clientService.getAll();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Client> update(@PathVariable Long id, @RequestBody Client client) {
    return clientService
        .update(id, client)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return clientService.delete(id)
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/search")
  public List<Client> search(@RequestParam String field, @RequestParam String value) {
    return clientService.getBy(field, value);
  }
}
