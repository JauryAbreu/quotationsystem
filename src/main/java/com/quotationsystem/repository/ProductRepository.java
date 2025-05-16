package com.quotationsystem.repository;

import com.quotationsystem.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Optional<List<Product>> findByName(String value);

  Optional<List<Product>> findByCode(String value);
}
