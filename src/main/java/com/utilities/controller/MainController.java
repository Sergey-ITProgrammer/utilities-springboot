package com.utilities.controller;


import com.utilities.domain.ScannedObject;
import com.utilities.repository.CommonRepository;
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
    private final CommonRepository<ScannedObject> repository;

    public MainController(CommonRepository<ScannedObject> repository) {
        this.repository = repository;
    }

    private List<Path> listOfFiles = new ArrayList<>();
    private List<Path> listOfBiggestFiles = new ArrayList<>();
    private Set<Path> listOfDuplicates = new LinkedHashSet<>();
    private Map<Path, String> listOfUnknownFiles = new LinkedHashMap<>();

    @Autowired
    AnalyzerService analyzerService;

    @PostMapping("")
    public void createScannedObject(@RequestBody ScannedObject object) {
        repository.save(object);
    }

    @PutMapping("")
    public void changeScannedObject(@RequestBody ScannedObject object) {
        ScannedObject existingScannedObject = repository.findById(object.getId());

        existingScannedObject.setPath(object.getPath());
    }

    @DeleteMapping("")
    public void deleteScannedObject(@RequestBody ScannedObject object) {
        repository.delete(object.getId());

        listOfFiles = Collections.emptyList();
        listOfBiggestFiles = Collections.emptyList();
        listOfDuplicates = Collections.emptySet();
        listOfUnknownFiles = Collections.emptyMap();
    }

    @GetMapping("/{id}")
    public List<Path> getAllFiles(@PathVariable String id) {
        if (!listOfFiles.isEmpty()) {
            listOfFiles = Collections.emptyList();
            listOfBiggestFiles = Collections.emptyList();
            listOfDuplicates = Collections.emptySet();
            listOfUnknownFiles = Collections.emptyMap();
        }

        ScavengerService scavengerService = new ScavengerService();

        if (listOfFiles.isEmpty()) {
            listOfFiles = scavengerService.findAll(repository.findById(id).getPath());
        }

        return listOfFiles;
    }

    @GetMapping("/{id}/biggestfiles/{amountOfFiles}")
    public List<Path> getBiggestFiles(@PathVariable String id, @PathVariable int amountOfFiles) {
        if (listOfBiggestFiles.isEmpty()) {
            listOfBiggestFiles = analyzerService.getBiggestFiles(listOfFiles, amountOfFiles);
        }

        return listOfBiggestFiles;
    }

    @GetMapping("/{id}/duplicates")
    public Set<Path> getDuplicates(@PathVariable String id) throws NoSuchAlgorithmException, IOException {
        if (listOfDuplicates.isEmpty()) {
            listOfDuplicates = analyzerService.getDuplicates(listOfFiles);
        }

        return listOfDuplicates;
    }

    @GetMapping("/{id}/unknownfiles")
    public Map<Path, String> getUnknownFiles(@PathVariable String id) throws NoSuchAlgorithmException, IOException {
        if (listOfUnknownFiles.isEmpty()) {
            listOfUnknownFiles = analyzerService.getUnknownFiles(listOfFiles);
        }

        return listOfUnknownFiles;
    }
}
