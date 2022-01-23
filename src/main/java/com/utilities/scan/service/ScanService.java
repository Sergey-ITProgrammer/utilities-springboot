package com.utilities.scan.service;

import com.utilities.domain.FilePath;
import com.utilities.domain.ScannedObject;
import com.utilities.repository.RepositoryOfFilesPath;
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
    private RepositoryOfFilesPath repositoryOfFilesPath;

    @Autowired
    private ScavengerServiceImpl scavengerService;

    public void createScannedObject(ScannedObject object) {
        ScannedObject scannedObject = repositoryOfScannedObjects.save(object);

        List<FilePath> pathList = scavengerService.findAll(object.getPath()).stream().map(p -> {
            FilePath filePath = new FilePath(p);
            filePath.setObject(scannedObject);

            return filePath;
        }).toList();

        repositoryOfFilesPath.saveAll(pathList);
    }

    @Transactional
    public ScannedObject changeScannedObject(long id, String newPath) {
        Optional<ScannedObject> scannedObject = repositoryOfScannedObjects.findById(id);

        if (scannedObject.isPresent()) {
            scannedObject.get().setPath(newPath);

            ScannedObject newScannedObject = repositoryOfScannedObjects.save(scannedObject.get());

            repositoryOfFilesPath.deleteByObjectId(id);

            List<FilePath> pathList = scavengerService.findAll(newPath).stream().map(p -> {
                FilePath filePath = new FilePath(p);
                filePath.setObject(newScannedObject);

                return filePath;
            }).toList();

            repositoryOfFilesPath.saveAll(pathList);

            return scannedObject.get();
        }

        return null;
    }

    @Transactional
    public void deleteScannedObject(long id) {
        Optional<ScannedObject> scannedObject = repositoryOfScannedObjects.findById(id);

        if (scannedObject.isPresent()) {
            repositoryOfFilesPath.deleteByObjectId(id);
            repositoryOfScannedObjects.deleteById(id);
        }
    }

    public Iterable<ScannedObject> getAllScannedObjects() {
        return repositoryOfScannedObjects.findAll();
    }

    public List<Path> getAllFiles(long id) {
        return repositoryOfFilesPath.findByObjectId(id).stream().map(p -> Path.of(p.getPath())).toList();
    }
}