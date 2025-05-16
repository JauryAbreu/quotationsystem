package com.quotationsystem.repository;

import com.quotationsystem.entity.DeliveryTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryTimeRepository extends JpaRepository<DeliveryTime, Long> {
  Optional<List<DeliveryTime>> findByDescription(String value);
}
