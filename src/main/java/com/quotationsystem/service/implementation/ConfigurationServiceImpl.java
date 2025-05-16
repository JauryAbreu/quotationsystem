package com.quotationsystem.service.implementation;

import com.quotationsystem.entity.Configuration;
import com.quotationsystem.repository.ConfigurationRepository;
import com.quotationsystem.service.interfaces.ServiceCommon;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfigurationServiceImpl implements ServiceCommon<Configuration, Long> {

  @Autowired private ConfigurationRepository configurationRepository;

  @Override
  public Configuration create(Configuration configuration) {
    return configurationRepository.save(configuration);
  }

  @Override
  public Optional<Configuration> get(Long id) {
    return configurationRepository.findById(id);
  }

  @Override
  public List<Configuration> getAll() {
    return configurationRepository.findAll();
  }

  @Override
  public Optional<Configuration> update(Long id, Configuration configuration) {
    return configurationRepository
        .findById(id)
        .map(
            existing -> {
              existing.setName(configuration.getName());
              existing.setAddress(configuration.getAddress());
              existing.setPhone(configuration.getPhone());
              existing.setRnc(configuration.getRnc());
              existing.setDraftPrefix(configuration.getDraftPrefix());
              existing.setPrefix(configuration.getPrefix());
              existing.setReportTemplate(configuration.getReportTemplate());
              return configurationRepository.save(existing);
            });
  }

  @Override
  public boolean delete(Long id) {
    return configurationRepository
        .findById(id)
        .map(
            configuration -> {
              configurationRepository.delete(configuration);
              return true;
            })
        .orElse(false);
  }

  @Override
  public List<Configuration> getBy(String field, Object value) {
    if (field.equals("rnc")) {
      return configurationRepository.findByRnc((String) value).orElse(List.of());
    }
    return List.of();
  }

  @Transactional
  public String getNextDraftSequence() {
    var config = configurationRepository.findFirst().orElseThrow();

    int current = config.getDraftSequence() != null ? config.getDraftSequence() : 0;
    int next = current + 1;
    config.setDraftSequence(next);
    configurationRepository.save(config);

    String prefix = config.getDraftPrefix() != null ? config.getDraftPrefix() : "";
    return prefix + String.format("%08d", next);
  }

  @Transactional
  public String getNextFinalSequence() {
    var config = configurationRepository.findFirst().orElseThrow();

    int current = config.getSequence() != null ? config.getSequence() : 0;
    int next = current + 1;
    config.setSequence(next);
    configurationRepository.save(config);

    String prefix = config.getPrefix() != null ? config.getPrefix() : "";
    return prefix + String.format("%08d", next);
  }
}
