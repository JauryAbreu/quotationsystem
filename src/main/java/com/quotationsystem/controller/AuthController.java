package com.quotationsystem.controller;

import com.quotationsystem.entity.User;
import com.quotationsystem.model.request.LoginRequest;
import com.quotationsystem.service.implementation.UserServiceImpl;
import com.quotationsystem.util.JwtUtil;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

  @Autowired private UserServiceImpl userService;

  @Autowired private JwtUtil jwtUtil;

  @Autowired private BCryptPasswordEncoder passwordEncoder;

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
    User user =
        userService.getBy("username", loginRequest.getUsername()).stream()
            .filter(u -> u.getPassword().equals(loginRequest.getPassword()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Invalid username or password"));

    String token = jwtUtil.generateToken(user.getUsername(), user.getRole().toString());
    Map<String, String> response = new HashMap<>();
    response.put("token", token);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/me")
  public ResponseEntity<User> getCurrentUser() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    if ("anonymousUser".equals(username)) {
      throw new RuntimeException("No authenticated user found");
    }
    User user =
        userService.getBy("username", username).stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("User not found"));
    return ResponseEntity.ok(user);
  }
}
