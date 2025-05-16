package com.quotationsystem.service.interfaces;

import java.util.List;
import java.util.Optional;

public interface ServiceCommon<T, ID> {
  T create(T entity);

  Optional<T> update(ID id, T entity);

  Optional<T> get(ID id);

  List<T> getAll();

  List<T> getBy(String field, Object value);

  boolean delete(ID id);
}
