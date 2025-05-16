package com.quotationsystem.service.implementation;

import com.quotationsystem.entity.Product;
import com.quotationsystem.repository.ProductRepository;
import com.quotationsystem.repository.TaxRepository;
import com.quotationsystem.service.interfaces.ServiceCommon;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ServiceCommon<Product, Long> {

  @Autowired private ProductRepository productRepository;

  @Autowired private TaxRepository taxRepository;

  @Override
  public Product create(Product product) {
    if (product.getTax() != null && product.getTax().getId() != null) {
      product.setTax(
          taxRepository
              .findById(product.getTax().getId())
              .orElseThrow(() -> new RuntimeException("Tax not found")));
    }
    return productRepository.save(product);
  }

  @Override
  public Optional<Product> get(Long id) {
    return productRepository.findById(id);
  }

  @Override
  public List<Product> getAll() {
    return productRepository.findAll();
  }

  @Override
  public Optional<Product> update(Long id, Product product) {
    return productRepository
        .findById(id)
        .map(
            existing -> {
              existing.setCode(product.getCode());
              existing.setName(product.getName());
              existing.setUnitPrice(product.getUnitPrice());
              if (product.getTax() != null && product.getTax().getId() != null) {
                existing.setTax(
                    taxRepository
                        .findById(product.getTax().getId())
                        .orElseThrow(() -> new RuntimeException("Tax not found")));
              } else {
                existing.setTax(null);
              }
              return productRepository.save(existing);
            });
  }

  @Override
  public boolean delete(Long id) {
    return productRepository
        .findById(id)
        .map(
            product -> {
              productRepository.delete(product);
              return true;
            })
        .orElse(false);
  }

  @Override
  public List<Product> getBy(String field, Object value) {
    if (field.equals("code")) {
      return productRepository.findByCode((String) value).orElse(List.of());
    }
    if (field.equals("name")) {
      return productRepository.findByName((String) value).orElse(List.of());
    }
    return List.of();
  }
}
