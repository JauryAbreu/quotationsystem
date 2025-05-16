package com.quotationsystem.controller;

import com.quotationsystem.entity.DeliveryTime;
import com.quotationsystem.service.implementation.DeliveryTimeServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("delivery-times")
public class DeliveryTimeController {

  @Autowired private DeliveryTimeServiceImpl deliveryTimeService;

  @PostMapping
  public DeliveryTime create(@RequestBody DeliveryTime deliveryTime) {
    return deliveryTimeService.create(deliveryTime);
  }

  @GetMapping("/{id}")
  public ResponseEntity<DeliveryTime> get(@PathVariable Long id) {
    return deliveryTimeService
        .get(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public List<DeliveryTime> getAll() {
    return deliveryTimeService.getAll();
  }

  @PutMapping("/{id}")
  public ResponseEntity<DeliveryTime> update(
      @PathVariable Long id, @RequestBody DeliveryTime deliveryTime) {
    return deliveryTimeService
        .update(id, deliveryTime)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return deliveryTimeService.delete(id)
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/search")
  public List<DeliveryTime> search(@RequestParam String field, @RequestParam String value) {
    return deliveryTimeService.getBy(field, value);
  }
}
