package com.utilities.service;

import com.utilities.domain.ScannedObject;
import com.utilities.repository.AllFilesRepositoryImpl;
import com.utilities.repository.CommonRepository;
import com.utilities.repository.ScannedObjectRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UtilitiesService {
    private final CommonRepository<ScannedObject> repositoryOfScannedObjects;
    private final CommonRepository<List<Path>> repositoryOfListsOfAllFiles;

    public UtilitiesService(ScannedObjectRepositoryImpl repositoryOfScannedObjects, AllFilesRepositoryImpl repositoryOfListsOfAllFiles) {
        this.repositoryOfScannedObjects = repositoryOfScannedObjects;
        this.repositoryOfListsOfAllFiles = repositoryOfListsOfAllFiles;
    }

    @Autowired
    AnalyzerService analyzerService;

    @Autowired
    ScavengerService scavengerService;

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

    public List<Path> getBiggestFiles(String id, int amountOfFiles) {
        analyzerService.cleanListOfBiggestFiles();

        return analyzerService.getBiggestFiles(repositoryOfListsOfAllFiles.findById(id), amountOfFiles);
    }

    public Set<Path> getDuplicates(String id) throws NoSuchAlgorithmException, IOException {
        analyzerService.cleanListOfDuplicates();

        return analyzerService.getDuplicates(repositoryOfListsOfAllFiles.findById(id));
    }

    public Map<Path, String> getUnknownFiles(String id) throws NoSuchAlgorithmException, IOException {
        analyzerService.cleanListOfUnknownFiles();

        return analyzerService.getUnknownFiles(repositoryOfListsOfAllFiles.findById(id));
    }
}
