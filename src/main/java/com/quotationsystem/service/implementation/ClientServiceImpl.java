package com.quotationsystem.service.implementation;

import com.quotationsystem.entity.Client;
import com.quotationsystem.repository.ClientRepository;
import com.quotationsystem.service.interfaces.ServiceCommon;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ServiceCommon<Client, Long> {

  @Autowired private ClientRepository clientRepository;

  @Override
  public Client create(Client client) {
    return clientRepository.save(client);
  }

  @Override
  public Optional<Client> update(Long id, Client client) {
    return clientRepository
        .findById(id)
        .map(
            existing -> {
              existing.setName(client.getName());
              existing.setRnc(client.getRnc());
              existing.setEmail(client.getEmail());
              existing.setPhone(client.getPhone());
              existing.setAddress(client.getAddress());
              return clientRepository.save(existing);
            });
  }

  @Override
  public Optional<Client> get(Long id) {
    return clientRepository.findById(id);
  }

  @Override
  public List<Client> getAll() {
    return clientRepository.findAll();
  }

  @Override
  public List<Client> getBy(String field, Object value) {
    if (field.equals("name")) {
      return clientRepository.findByName((String) value).orElse(List.of());
    } else if (field.equals("rnc")) {
      return clientRepository.findByRnc((String) value).orElse(List.of());
    } else if (field.equals("email")) {
      return clientRepository.findByEmail((String) value).orElse(List.of());
    }
    return List.of();
  }

  @Override
  public boolean delete(Long id) {
    return clientRepository
        .findById(id)
        .map(
            client -> {
              clientRepository.delete(client);
              return true;
            })
        .orElse(false);
  }
}
