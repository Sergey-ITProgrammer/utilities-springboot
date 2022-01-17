package com.utilities.scan.service;

import com.utilities.domain.ScannedObject;
import com.utilities.repository.RepositoryOfScannedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class ScanService {

    @Autowired
    private RepositoryOfScannedObject repositoryOfScannedObjects;

    @Autowired
    private ScavengerServiceImpl scavengerService;

    public void createScannedObject(ScannedObject object) {
        scavengerService.cleanList();

        repositoryOfScannedObjects.save(object);
    }

    public ScannedObject changeScannedObject(long id, String newPath) {
        scavengerService.cleanList();

        Optional<ScannedObject> scannedObject = repositoryOfScannedObjects.findById(id);

        if (scannedObject.isPresent()) {
            scannedObject.get().setPath(newPath);

            repositoryOfScannedObjects.save(scannedObject.get());

            return scannedObject.get();
        }

        return null;
    }

    public void deleteScannedObject(long id) {
        Optional<ScannedObject> scannedObject = repositoryOfScannedObjects.findById(id);

        scannedObject.ifPresent(repositoryOfScannedObjects::delete);
    }

    public Iterable<ScannedObject> getAllScannedObjects() {
        return repositoryOfScannedObjects.findAll();
    }

    public List<Path> getAllFiles(long id) {
        Optional<ScannedObject> scannedObject = repositoryOfScannedObjects.findById(id);

        if(scannedObject.isPresent()) {
            return scavengerService.findAll(scannedObject.get().getPath());
        }

        return null;
    }
}