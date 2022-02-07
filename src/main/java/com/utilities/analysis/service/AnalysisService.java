package com.utilities.analysis.service;

import com.utilities.domain.ScannedObject;
import com.utilities.repository.RepositoryOfScannedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AnalysisService {
    @Autowired
    private RepositoryOfScannedObject repositoryOfScannedObject;

    @Autowired
    private AnalyzerServiceImpl analyzerService;

    public List<Path> getBiggestFiles(long id, int amountOfFiles) {
        ScannedObject scannedObject = repositoryOfScannedObject.getById(id);

        List<Path> list = scannedObject.getAllFilesList().stream().map(Path::of).toList();

        return analyzerService.getBiggestFiles(list, amountOfFiles);
    }

    public Set<Path> getDuplicates(long id) throws NoSuchAlgorithmException, IOException {
        ScannedObject scannedObject = repositoryOfScannedObject.getById(id);

        List<Path> list = scannedObject.getAllFilesList().stream().map(Path::of).toList();

        return analyzerService.getDuplicates(list);
    }

    public Map<Path, String> getUnknownFiles(long id) throws NoSuchAlgorithmException, IOException {
        ScannedObject scannedObject = repositoryOfScannedObject.getById(id);

        List<Path> list = scannedObject.getAllFilesList().stream().map(Path::of).toList();

        return analyzerService.getUnknownFiles(list);
    }
}
