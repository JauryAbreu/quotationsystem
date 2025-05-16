package com.quotationsystem.service.implementation;

import com.quotationsystem.entity.Department;
import com.quotationsystem.repository.DepartmentRepository;
import com.quotationsystem.service.interfaces.ServiceCommon;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements ServiceCommon<Department, Long> {

  @Autowired private DepartmentRepository departmentRepository;

  @Override
  public Department create(Department department) {
    return departmentRepository.save(department);
  }

  @Override
  public Optional<Department> get(Long id) {
    return departmentRepository.findById(id);
  }

  @Override
  public List<Department> getAll() {
    return departmentRepository.findAll();
  }

  @Override
  public Optional<Department> update(Long id, Department department) {
    return departmentRepository
        .findById(id)
        .map(
            existing -> {
              existing.setName(department.getName());
              return departmentRepository.save(existing);
            });
  }

  @Override
  public boolean delete(Long id) {
    return departmentRepository
        .findById(id)
        .map(
            department -> {
              departmentRepository.delete(department);
              return true;
            })
        .orElse(false);
  }

  @Override
  public List<Department> getBy(String field, Object value) {
    if (field.equals("name")) {
      return departmentRepository.findByName((String) value).orElse(List.of());
    }
    return List.of();
  }
}
