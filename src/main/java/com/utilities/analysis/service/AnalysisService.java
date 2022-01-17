package com.utilities.analysis.service;

import com.utilities.domain.ScannedObject;
import com.utilities.repository.RepositoryOfScannedObject;
import com.utilities.scan.service.ScavengerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class AnalysisService {

    @Autowired
    private RepositoryOfScannedObject repositoryOfScannedObjects;

    @Autowired
    private ScavengerServiceImpl scavengerService;

    @Autowired
    private AnalyzerServiceImpl analyzerService;

    public List<Path> getBiggestFiles(long id, int amountOfFiles) {
        analyzerService.cleanListOfBiggestFiles();

        Optional<ScannedObject> scannedObject = repositoryOfScannedObjects.findById(id);

        if (scannedObject.isPresent()) {
            List<Path> list = scavengerService.findAll(scannedObject.get().getPath());

            return analyzerService.getBiggestFiles(list, amountOfFiles);
        }

        return null;
    }

    public Set<Path> getDuplicates(long id) throws NoSuchAlgorithmException, IOException {
        analyzerService.cleanListOfDuplicates();

        Optional<ScannedObject> scannedObject = repositoryOfScannedObjects.findById(id);

        if (scannedObject.isPresent()) {
            List<Path> list = scavengerService.findAll(scannedObject.get().getPath());

            return analyzerService.getDuplicates(list);
        }

        return null;
    }

    public Map<Path, String> getUnknownFiles(long id) throws NoSuchAlgorithmException, IOException {
        analyzerService.cleanListOfUnknownFiles();

        Optional<ScannedObject> scannedObject = repositoryOfScannedObjects.findById(id);

        if (scannedObject.isPresent()) {
            List<Path> list = scavengerService.findAll(scannedObject.get().getPath());

            return analyzerService.getUnknownFiles(list);
        }

        return null;
    }
}
