package com.utilities.controller;


import com.utilities.domain.ScannedObject;
import com.utilities.repository.AllFilesRepositoryImpl;
import com.utilities.repository.CommonRepository;
import com.utilities.repository.ScannedObjectRepositoryImpl;
import com.utilities.service.AnalyzerService;
import com.utilities.service.ScavengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@RequestMapping("/utilities")
public class MainController {
    private final CommonRepository<ScannedObject> repositoryOfScannedObjects;
    private final CommonRepository<List<Path>> repositoryOfListsOfAllFiles;

    public MainController(ScannedObjectRepositoryImpl repositoryOfScannedObjects, AllFilesRepositoryImpl repositoryOfListsOfAllFiles) {
        this.repositoryOfScannedObjects = repositoryOfScannedObjects;
        this.repositoryOfListsOfAllFiles = repositoryOfListsOfAllFiles;
    }

    @Autowired
    AnalyzerService analyzerService;

    @Autowired
    ScavengerService scavengerService;

    @PostMapping("")
    public void createScannedObject(@RequestBody ScannedObject object) {
        System.out.println(object.getPath());
        System.out.println(object.getId());

        scavengerService.cleanList();

        repositoryOfScannedObjects.save(object, object.getId());
        repositoryOfListsOfAllFiles.save(scavengerService.findAll(object.getPath()), object.getId());
    }

    @PutMapping("")
    public void changeScannedObject(@RequestBody ScannedObject object) {
        scavengerService.cleanList();

        repositoryOfScannedObjects.setNewValue(object, object.getId());
        repositoryOfListsOfAllFiles.setNewValue(scavengerService.findAll(object.getPath()), object.getId());
    }

    @DeleteMapping("")
    public void deleteScannedObject(@RequestBody ScannedObject object) {
        repositoryOfScannedObjects.delete(object.getId());
        repositoryOfListsOfAllFiles.delete(object.getId());
    }

    @GetMapping("/{id}")
    public List<Path> getAllFiles(@PathVariable String id) {
        return repositoryOfListsOfAllFiles.findById(id);
    }

    @GetMapping("/{id}/biggestfiles/{amountOfFiles}")
    public List<Path> getBiggestFiles(@PathVariable String id, @PathVariable int amountOfFiles) {
        analyzerService.cleanListOfBiggestFiles();

        return analyzerService.getBiggestFiles(repositoryOfListsOfAllFiles.findById(id), amountOfFiles);
    }

    @GetMapping("/{id}/duplicates")
    public Set<Path> getDuplicates(@PathVariable String id) throws NoSuchAlgorithmException, IOException {
        analyzerService.cleanListOfDuplicates();

        return analyzerService.getDuplicates(repositoryOfListsOfAllFiles.findById(id));
    }

    @GetMapping("/{id}/unknownfiles")
    public Map<Path, String> getUnknownFiles(@PathVariable String id) throws NoSuchAlgorithmException, IOException {
        analyzerService.cleanListOfUnknownFiles();

        return analyzerService.getUnknownFiles(repositoryOfListsOfAllFiles.findById(id));
    }
}
