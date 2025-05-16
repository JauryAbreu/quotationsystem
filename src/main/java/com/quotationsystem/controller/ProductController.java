package com.quotationsystem.controller;

import com.quotationsystem.entity.Product;
import com.quotationsystem.service.implementation.ProductServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("products")
public class ProductController {

  @Autowired private ProductServiceImpl productService;

  @PostMapping
  public Product create(@RequestBody Product product) {
    return productService.create(product);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> get(@PathVariable Long id) {
    return productService
        .get(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public List<Product> getAll() {
    return productService.getAll();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
    return productService
        .update(id, product)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return productService.delete(id)
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/search")
  public List<Product> search(@RequestParam String field, @RequestParam String value) {
    return productService.getBy(field, value);
  }
}
