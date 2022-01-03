package com.utilities.analysis.service;

import com.utilities.repository.AllFilesRepositoryImpl;
import com.utilities.repository.CommonRepository;
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
    private final CommonRepository<List<Path>> repositoryOfListsOfAllFiles;

    public AnalysisService(AllFilesRepositoryImpl repositoryOfListsOfAllFiles) {
        this.repositoryOfListsOfAllFiles = repositoryOfListsOfAllFiles;
    }

    @Autowired
    AnalyzerServiceImpl analyzerService;

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
