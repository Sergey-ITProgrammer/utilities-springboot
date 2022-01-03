package com.utilities.scan.service;

import com.utilities.domain.ScannedObject;
import com.utilities.repository.AllFilesRepositoryImpl;
import com.utilities.repository.CommonRepository;
import com.utilities.repository.ScannedObjectRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Service
public class ScanService {
    private final CommonRepository<ScannedObject> repositoryOfScannedObjects;
    private final CommonRepository<List<Path>> repositoryOfListsOfAllFiles;

    public ScanService(ScannedObjectRepositoryImpl repositoryOfScannedObjects, AllFilesRepositoryImpl repositoryOfListsOfAllFiles) {
        this.repositoryOfScannedObjects = repositoryOfScannedObjects;
        this.repositoryOfListsOfAllFiles = repositoryOfListsOfAllFiles;
    }

    @Autowired
    ScavengerServiceImpl scavengerService;

    public void createScannedObject(ScannedObject object) {
        scavengerService.cleanList();

        repositoryOfScannedObjects.save(object, object.getId());
        repositoryOfListsOfAllFiles.save(scavengerService.findAll(object.getPath()), object.getId());
    }

    public void changeScannedObject(String id, String newPath) {
        scavengerService.cleanList();

        ScannedObject newObject = repositoryOfScannedObjects.findById(id);
        newObject.setPath(newPath);

        repositoryOfScannedObjects.setNewValue(id, newObject);
        repositoryOfListsOfAllFiles.setNewValue(id, scavengerService.findAll(newPath));
    }

    public void deleteScannedObject(String id) {
        repositoryOfScannedObjects.delete(id);
        repositoryOfListsOfAllFiles.delete(id);
    }

    public Map<String, ScannedObject> getAllScannedObjects() {
        return repositoryOfScannedObjects.getAll();
    }

    public List<Path> getAllFiles(String id) {
        return repositoryOfListsOfAllFiles.findById(id);
    }
}
