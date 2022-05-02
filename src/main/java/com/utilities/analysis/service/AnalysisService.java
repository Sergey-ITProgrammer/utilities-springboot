package com.utilities.analysis.service;

import com.utilities.domain.ScannedObject;
import com.utilities.repository.RepositoryOfScannedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AnalysisService {
    private final RepositoryOfScannedObject repositoryOfScannedObject;
    private final AnalyzerServiceImpl analyzerService;

    @Autowired
    public AnalysisService(RepositoryOfScannedObject repositoryOfScannedObject, AnalyzerServiceImpl analyzerService) {
        this.repositoryOfScannedObject = repositoryOfScannedObject;
        this.analyzerService = analyzerService;
    }

    public List<Path> getBiggestFiles(long id, int amountOfFiles) {
        ScannedObject scannedObject = repositoryOfScannedObject.getById(id);

        List<Path> list = scannedObject.getAllFilesList().stream().map(p -> Path.of(p.getPath())).toList();

        return analyzerService.getBiggestFiles(list, amountOfFiles);
    }

    public Set<Path> getDuplicates(long id) {
        ScannedObject scannedObject = repositoryOfScannedObject.getById(id);

        List<Path> list = scannedObject.getAllFilesList().stream().map(p -> Path.of(p.getPath())).toList();

        return analyzerService.getDuplicates(list);
    }

    public Map<Path, String> getUnknownFiles(long id) {
        ScannedObject scannedObject = repositoryOfScannedObject.getById(id);

        List<Path> list = scannedObject.getAllFilesList().stream().map(p -> Path.of(p.getPath())).toList();

        return analyzerService.getUnknownFiles(list);
    }
}
