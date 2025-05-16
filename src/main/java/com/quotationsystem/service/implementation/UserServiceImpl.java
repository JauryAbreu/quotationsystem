package com.quotationsystem.service.implementation;

import com.quotationsystem.entity.User;
import com.quotationsystem.repository.UserRepository;
import com.quotationsystem.service.interfaces.ServiceCommon;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements ServiceCommon<User, Long> {

  @Autowired private UserRepository userRepository;

  @Override
  public User create(User user) {
    // Only admin can create users, this should be checked in the controller
    return userRepository.save(user);
  }

  @Override
  public Optional<User> get(Long id) {
    return userRepository.findById(id);
  }

  @Override
  public List<User> getAll() {
    return userRepository.findAll();
  }

  @Override
  public Optional<User> update(Long id, User user) {
    return userRepository
        .findById(id)
        .map(
            existing -> {
              existing.setUsername(user.getUsername());
              existing.setPassword(user.getPassword());
              existing.setRole(user.getRole());
              return userRepository.save(existing);
            });
  }

  @Override
  public boolean delete(Long id) {
    return userRepository
        .findById(id)
        .map(
            user -> {
              userRepository.delete(user);
              return true;
            })
        .orElse(false);
  }

  @Override
  public List<User> getBy(String field, Object value) {
    if (field.equals("username")) {
      return userRepository.findByUsername((String) value).orElse(List.of());
    }
    return List.of();
  }
}
