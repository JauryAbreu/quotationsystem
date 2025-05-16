package com.quotationsystem.service.implementation;

import com.quotationsystem.entity.DeliveryTime;
import com.quotationsystem.repository.DeliveryTimeRepository;
import com.quotationsystem.service.interfaces.ServiceCommon;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryTimeServiceImpl implements ServiceCommon<DeliveryTime, Long> {

  @Autowired private DeliveryTimeRepository deliveryTimeRepository;

  @Override
  public DeliveryTime create(DeliveryTime deliveryTime) {
    return deliveryTimeRepository.save(deliveryTime);
  }

  @Override
  public Optional<DeliveryTime> get(Long id) {
    return deliveryTimeRepository.findById(id);
  }

  @Override
  public List<DeliveryTime> getAll() {
    return deliveryTimeRepository.findAll();
  }

  @Override
  public Optional<DeliveryTime> update(Long id, DeliveryTime deliveryTime) {
    return deliveryTimeRepository
        .findById(id)
        .map(
            existing -> {
              existing.setDescription(deliveryTime.getDescription());
              existing.setDays(deliveryTime.getDays());
              return deliveryTimeRepository.save(existing);
            });
  }

  @Override
  public boolean delete(Long id) {
    return deliveryTimeRepository
        .findById(id)
        .map(
            deliveryTime -> {
              deliveryTimeRepository.delete(deliveryTime);
              return true;
            })
        .orElse(false);
  }

  @Override
  public List<DeliveryTime> getBy(String field, Object value) {
    if (field.equals("description")) {
      return deliveryTimeRepository.findByDescription((String) value).orElse(List.of());
    }
    return List.of();
  }
}
