package com.utilities.repository;

import com.utilities.domain.ScannedObject;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ScannedObjectRepositoryImpl implements CommonRepository<ScannedObject>{
    private final Map<String, ScannedObject> scannedObjects = new HashMap<>();

    @Override
    public Map<String, ScannedObject> getAll() {
        return scannedObjects;
    }

    @Override
    public void save(ScannedObject domain, String id) {
        scannedObjects.put(id, domain);
    }

    @Override
    public void delete(String id) {
        scannedObjects.remove(id);
    }

    @Override
    public ScannedObject findById(String id) {
        return scannedObjects.get(id);
    }

    @Override
    public void setNewValue(String id, ScannedObject newObject) {
        scannedObjects.replace(id, newObject);
    }
}
