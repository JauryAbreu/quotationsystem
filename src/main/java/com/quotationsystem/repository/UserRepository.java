package com.quotationsystem.repository;

import com.quotationsystem.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<List<User>> findByUsername(String value);
}
