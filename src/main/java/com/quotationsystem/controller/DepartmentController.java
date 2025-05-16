package com.quotationsystem.controller;

import com.quotationsystem.entity.Department;
import com.quotationsystem.service.implementation.DepartmentServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("departments")
public class DepartmentController {

  @Autowired private DepartmentServiceImpl departmentService;

  @PostMapping
  public Department create(@RequestBody Department department) {
    return departmentService.create(department);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Department> get(@PathVariable Long id) {
    return departmentService
        .get(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public List<Department> getAll() {
    return departmentService.getAll();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Department> update(
      @PathVariable Long id, @RequestBody Department department) {
    return departmentService
        .update(id, department)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return departmentService.delete(id)
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/search")
  public List<Department> search(@RequestParam String field, @RequestParam String value) {
    return departmentService.getBy(field, value);
  }
}
