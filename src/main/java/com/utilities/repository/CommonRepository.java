package com.utilities.repository;

public interface CommonRepository<T> {
    public T save(T domain);
    public void delete(String id);
    public T findById(String id);
}
