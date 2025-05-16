package com.quotationsystem.service.implementation;

import com.quotationsystem.entity.Tax;
import com.quotationsystem.repository.TaxRepository;
import com.quotationsystem.service.interfaces.ServiceCommon;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxServiceImpl implements ServiceCommon<Tax, Long> {

  @Autowired private TaxRepository taxRepository;

  @Override
  public Tax create(Tax tax) {
    return taxRepository.save(tax);
  }

  @Override
  public Optional<Tax> get(Long id) {
    return taxRepository.findById(id);
  }

  @Override
  public List<Tax> getAll() {
    return taxRepository.findAll();
  }

  @Override
  public Optional<Tax> update(Long id, Tax tax) {
    return taxRepository
        .findById(id)
        .map(
            existing -> {
              existing.setName(tax.getName());
              existing.setRate(tax.getRate());
              return taxRepository.save(existing);
            });
  }

  @Override
  public boolean delete(Long id) {
    return taxRepository
        .findById(id)
        .map(
            tax -> {
              taxRepository.delete(tax);
              return true;
            })
        .orElse(false);
  }

  @Override
  public List<Tax> getBy(String field, Object value) {
    if (field.equals("name")) {
      return taxRepository.findByName((String) value).orElse(List.of());
    }
    return List.of();
  }
}
