package com.quotationsystem.repository;

import com.quotationsystem.entity.Client;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
  Optional<List<Client>> findByName(String value);

  Optional<List<Client>> findByRnc(String value);

  Optional<List<Client>> findByEmail(String value);
}
