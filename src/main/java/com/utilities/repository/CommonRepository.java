package com.utilities.repository;

public interface CommonRepository<T> {
    void save(T domain, String id);
    void delete(String id);
    T findById(String id);
    void setValue(T value, String id);
}
