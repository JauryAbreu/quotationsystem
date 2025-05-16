package com.quotationsystem.repository;

import com.quotationsystem.entity.Seller;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
  Optional<List<Seller>> findByName(String value);
}
