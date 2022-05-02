package com.utilities.scan.service;

import com.utilities.domain.FilePath;
import com.utilities.domain.ScannedObject;
import com.utilities.repository.RepositoryOfScannedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ScanService {

    private final RepositoryOfScannedObject repositoryOfScannedObjects;

    private final ScavengerServiceImpl scavengerService;

    @Autowired
    public ScanService(RepositoryOfScannedObject repositoryOfScannedObjects, ScavengerServiceImpl scavengerService) {
        this.repositoryOfScannedObjects = repositoryOfScannedObjects;
        this.scavengerService = scavengerService;
    }

    public void createScannedObject(ScannedObject object) {
        List<FilePath> list = scavengerService.findAll(object.getPath()).stream()
                .map(p -> new FilePath(p.toString(), object)).toList();

        object.setAllFilesList(list);

        repositoryOfScannedObjects.save(object);
    }

    @Transactional
    public ScannedObject changeScannedObject(long id, String newPath) {
        Optional<ScannedObject> scannedObject = repositoryOfScannedObjects.findById(id);

        if (scannedObject.isPresent()) {
            scannedObject.get().setPath(newPath);

            List<FilePath> list = scavengerService.findAll(newPath).stream()
                    .map(p -> new FilePath(p.toString(), scannedObject.get())).toList();

            scannedObject.get().setAllFilesList(list);

            repositoryOfScannedObjects.save(scannedObject.get());

            return scannedObject.get();
        }

        return null;
    }

    public List<ScannedObject> getAllScannedObjects() {
        return repositoryOfScannedObjects.findAll();
    }

    public List<String> getAllFiles(long id) {
        ScannedObject scannedObject = repositoryOfScannedObjects.getById(id);

        return scannedObject.getAllFilesList().stream().map(FilePath::getPath).toList();
    }

    @Transactional
    public void deleteScannedObject(long id) {
        Optional<ScannedObject> scannedObject = repositoryOfScannedObjects.findById(id);

        if (scannedObject.isPresent()) {
            repositoryOfScannedObjects.deleteById(id);
        }
    }
}