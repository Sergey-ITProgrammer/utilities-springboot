package com.utilities.analysis.service;

import com.utilities.repository.RepositoryOfFilesPath;
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
    private RepositoryOfFilesPath repositoryOfFilesPath;

    @Autowired
    private AnalyzerServiceImpl analyzerService;

    public List<Path> getBiggestFiles(long id, int amountOfFiles) {
        List<Path> list = repositoryOfFilesPath.findByObjectId(id).stream().map(p -> Path.of(p.getPath())).toList();

        return analyzerService.getBiggestFiles(list, amountOfFiles);
    }

    public Set<Path> getDuplicates(long id) throws NoSuchAlgorithmException, IOException {
        List<Path> list = repositoryOfFilesPath.findByObjectId(id).stream().map(p -> Path.of(p.getPath())).toList();

        return analyzerService.getDuplicates(list);
    }

    public Map<Path, String> getUnknownFiles(long id) throws NoSuchAlgorithmException, IOException {
        List<Path> list = repositoryOfFilesPath.findByObjectId(id).stream().map(p -> Path.of(p.getPath())).toList();

        return analyzerService.getUnknownFiles(list);
    }
}
