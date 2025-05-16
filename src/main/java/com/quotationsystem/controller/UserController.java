package com.quotationsystem.controller;

import com.quotationsystem.entity.User;
import com.quotationsystem.service.implementation.UserServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

  @Autowired private UserServiceImpl userService;

  @PostMapping
  public User create(@RequestBody User user) {
    return userService.create(user);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> get(@PathVariable Long id) {
    return userService
        .get(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public List<User> getAll() {
    return userService.getAll();
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
    return userService
        .update(id, user)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return userService.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
  }

  @GetMapping("/search")
  public List<User> search(@RequestParam String field, @RequestParam String value) {
    return userService.getBy(field, value);
  }
}
