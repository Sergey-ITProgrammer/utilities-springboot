package com.utilities.scan.service;

import com.utilities.domain.ScannedObject;
import com.utilities.repository.RepositoryOfScannedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<String> list = scavengerService.findAll(object.getPath()).stream().map(Path::toString).toList();

        object.setAllFilesList(list);

        repositoryOfScannedObjects.save(object);
    }

    @Transactional
    public ScannedObject changeScannedObject(long id, String newPath) {
        Optional<ScannedObject> scannedObject = repositoryOfScannedObjects.findById(id);

        if (scannedObject.isPresent()) {
            scannedObject.get().setPath(newPath);

            List<String> list = scavengerService.findAll(newPath).stream().map(Path::toString).toList();

            scannedObject.get().setAllFilesList(list);

            repositoryOfScannedObjects.save(scannedObject.get());

            return scannedObject.get();
        }

        return null;
    }

    @Transactional
    public void deleteScannedObject(long id) {
        Optional<ScannedObject> scannedObject = repositoryOfScannedObjects.findById(id);

        if (scannedObject.isPresent()) {
            repositoryOfScannedObjects.deleteById(id);
        }
    }

    public Iterable<ScannedObject> getAllScannedObjects() {
        return repositoryOfScannedObjects.findAll();
    }

    public List<String> getAllFiles(long id) {
        ScannedObject scannedObject = repositoryOfScannedObjects.getById(id);

        return scannedObject.getAllFilesList();
    }
}