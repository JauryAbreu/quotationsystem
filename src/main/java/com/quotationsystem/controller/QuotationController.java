package com.quotationsystem.controller;

import com.quotationsystem.entity.QuotationHeader;
import com.quotationsystem.model.request.ProductRequest;
import com.quotationsystem.service.implementation.QuotationServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("quotations")
public class QuotationController {

  @Autowired private QuotationServiceImpl quotationService;

  @PostMapping
  public QuotationHeader create(@RequestBody QuotationHeader quotationHeader) {
    try {
      return quotationService.create(quotationHeader);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<QuotationHeader> get(@PathVariable Long id) {
    return quotationService
        .get(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/recent")
  public List<QuotationHeader> getRecent(@RequestParam int limit) {
    return quotationService.getRecent(limit);
  }

  @GetMapping("")
  public List<QuotationHeader> getAll() {
    return quotationService.getAll();
  }

  @PutMapping("/{id}")
  public ResponseEntity<QuotationHeader> update(
      @PathVariable Long id, @RequestBody QuotationHeader quotationHeader) {
    return quotationService
        .update(id, quotationHeader)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return quotationService.delete(id)
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }

  @PostMapping("/{id}/reissue")
  public QuotationHeader reissue(@PathVariable Long id) {
    return quotationService.reissue(id);
  }

  @PostMapping("/{headerId}/add-product")
  public QuotationHeader addProduct(
      @PathVariable Long headerId,
      @RequestBody ProductRequest product) {
    return quotationService.addProduct(headerId, product.getProductId(), product.getQuotedPrice(), product.getQuantity());
  }

  @PostMapping("/{id}/approve")
  public QuotationHeader approve(@PathVariable Long id, @RequestParam String quotationNumber) {
    return quotationService.approve(id, quotationNumber);
  }

  @DeleteMapping("/{headerId}/product/{detailId}")
  public QuotationHeader removeProduct(@PathVariable Long headerId, @PathVariable Long detailId) {
    return quotationService.removeProduct(headerId, detailId);
  }

  @PutMapping("/{headerId}/product/{detailId}")
  public QuotationHeader modifyProduct(
      @PathVariable Long headerId,
      @PathVariable Long detailId,
      @RequestParam(required = false) Double quotedPrice,
      @RequestParam(required = false) Integer quantity) {
    return quotationService.modifyProduct(headerId, detailId, quotedPrice, quantity);
  }

  @GetMapping("/search")
  public List<QuotationHeader> search(@RequestParam String field, @RequestParam String value) {
    return quotationService.getBy(field, value);
  }
}
