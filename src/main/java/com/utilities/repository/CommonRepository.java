package com.utilities.repository;

import com.utilities.domain.ScannedObject;

import java.util.Map;
import java.util.Set;

public interface CommonRepository<T> {
    public T save(T domain);
    public void delete(T domain);
    public T findById(String id);
}
