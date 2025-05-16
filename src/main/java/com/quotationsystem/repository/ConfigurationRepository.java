package com.quotationsystem.repository;

import com.quotationsystem.entity.Configuration;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
  Optional<List<Configuration>> findByRnc(String value);

  @Query("SELECT c FROM Configuration c ORDER BY c.id ASC")
  Optional<Configuration> findFirst();
}
