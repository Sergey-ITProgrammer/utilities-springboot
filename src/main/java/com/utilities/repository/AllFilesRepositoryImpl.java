package com.utilities.repository;

import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AllFilesRepositoryImpl implements CommonRepository<List<Path>> {
    private final Map<String, List<Path>> listsOfAllFiles = new LinkedHashMap<>();


    @Override
    public Map<String, List<Path>> getAll() {
        return listsOfAllFiles;
    }

    @Override
    public void save(List<Path> list, String id) {
        listsOfAllFiles.put(id, list);
    }

    @Override
    public void delete(String id) {
        listsOfAllFiles.remove(id);
    }

    @Override
    public List<Path> findById(String id) {
        return listsOfAllFiles.get(id);
    }

    @Override
    public void setNewValue(String id, List<Path> value) {
        listsOfAllFiles.replace(id, value);
    }
}
