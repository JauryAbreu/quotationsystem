package com.quotationsystem.controller;

import com.quotationsystem.entity.Configuration;
import com.quotationsystem.service.implementation.ConfigurationServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("configurations")
public class ConfigurationController {

  @Autowired private ConfigurationServiceImpl configurationService;

  @PostMapping
  public Configuration create(@RequestBody Configuration configuration) {
    return configurationService.create(configuration);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Configuration> get(@PathVariable Long id) {
    return configurationService
        .get(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public List<Configuration> getAll() {
    return configurationService.getAll();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Configuration> update(
      @PathVariable Long id, @RequestBody Configuration configuration) {
    return configurationService
        .update(id, configuration)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return configurationService.delete(id)
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/search")
  public List<Configuration> search(@RequestParam String field, @RequestParam String value) {
    return configurationService.getBy(field, value);
  }
}
