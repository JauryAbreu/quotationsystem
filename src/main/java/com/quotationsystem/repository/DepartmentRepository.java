package com.quotationsystem.repository;

import com.quotationsystem.entity.Department;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
  Optional<List<Department>> findByName(String value);
}
