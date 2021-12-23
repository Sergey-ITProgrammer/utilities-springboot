package com.utilities.repository;

import com.utilities.domain.ScannedObject;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
public class ScannedObjectRepositoryImpl implements CommonRepository<ScannedObject>{
    private final Map<String, ScannedObject> scannedObjects = new HashMap<>();

    @Override
    public ScannedObject save(ScannedObject domain) {
        scannedObjects.put(domain.getId(), domain);

        return scannedObjects.get(domain.getId());
    }

    @Override
    public void delete(String id) {
        scannedObjects.remove(id);
    }

    @Override
    public ScannedObject findById(String id) {
        return scannedObjects.get(id);
    }
}
