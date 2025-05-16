package com.quotationsystem.service.implementation;

import com.quotationsystem.entity.Seller;
import com.quotationsystem.repository.SellerRepository;
import com.quotationsystem.service.interfaces.ServiceCommon;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements ServiceCommon<Seller, Long> {

  @Autowired private SellerRepository sellerRepository;

  @Override
  public Seller create(Seller seller) {
    return sellerRepository.save(seller);
  }

  @Override
  public Optional<Seller> update(Long id, Seller seller) {
    return sellerRepository
        .findById(id)
        .map(
            existing -> {
              existing.setName(seller.getName());
              return sellerRepository.save(existing);
            });
  }

  @Override
  public Optional<Seller> get(Long id) {
    return sellerRepository.findById(id);
  }

  @Override
  public List<Seller> getAll() {
    return sellerRepository.findAll();
  }

  @Override
  public List<Seller> getBy(String field, Object value) {
    if (field.equals("name")) {
      return sellerRepository.findByName((String) value).orElse(List.of());
    }
    return List.of();
  }

  @Override
  public boolean delete(Long id) {
    return sellerRepository
        .findById(id)
        .map(
            seller -> {
              sellerRepository.delete(seller);
              return true;
            })
        .orElse(false);
  }
}
