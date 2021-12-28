package com.utilities.repository;

import java.util.Map;

public interface CommonRepository<T> {
    Map<String, T> getAll();
    void save(T domain, String id);
    void delete(String id);
    T findById(String id);
    void setNewValue(String id, T newObject);
}
