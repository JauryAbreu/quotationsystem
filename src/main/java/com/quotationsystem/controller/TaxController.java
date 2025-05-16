package com.quotationsystem.controller;

import com.quotationsystem.entity.Tax;
import com.quotationsystem.service.implementation.TaxServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("taxes")
public class TaxController {

  @Autowired private TaxServiceImpl taxService;

  @PostMapping
  public Tax create(@RequestBody Tax tax) {
    return taxService.create(tax);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Tax> get(@PathVariable Long id) {
    return taxService
        .get(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public List<Tax> getAll() {
    return taxService.getAll();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Tax> update(@PathVariable Long id, @RequestBody Tax tax) {
    return taxService
        .update(id, tax)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return taxService.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
  }

  @GetMapping("/search")
  public List<Tax> search(@RequestParam String field, @RequestParam String value) {
    return taxService.getBy(field, value);
  }
}
