package com.quotationsystem.repository;

import com.quotationsystem.entity.Tax;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRepository extends JpaRepository<Tax, Long> {
  Optional<List<Tax>> findByName(String value);
}
