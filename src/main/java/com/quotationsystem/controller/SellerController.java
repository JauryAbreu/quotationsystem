package com.quotationsystem.controller;

import com.quotationsystem.entity.Seller;
import com.quotationsystem.service.implementation.SellerServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("sellers")
public class SellerController {

  @Autowired private SellerServiceImpl sellerService;

  @PostMapping
  public Seller create(@RequestBody Seller seller) {
    return sellerService.create(seller);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Seller> get(@PathVariable Long id) {
    return sellerService
        .get(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public List<Seller> getAll() {
    return sellerService.getAll();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Seller> update(@PathVariable Long id, @RequestBody Seller seller) {
    return sellerService
        .update(id, seller)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return sellerService.delete(id)
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/search")
  public List<Seller> search(@RequestParam String field, @RequestParam String value) {
    return sellerService.getBy(field, value);
  }
}
