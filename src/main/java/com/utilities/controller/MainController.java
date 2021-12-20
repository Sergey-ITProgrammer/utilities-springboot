package com.utilities.controller;


import com.utilities.domain.ScannedObject;
import com.utilities.repository.CommonRepository;
import com.utilities.service.AnalyzerService;
import com.utilities.service.ScavengerService;
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

    @PostMapping("")
    public void createScannedObject(@RequestBody ScannedObject object) {
        System.out.println(object.getPath());
        System.out.println(object.getId());

        repository.save(object);
    }

    @GetMapping("/{id}")
    public List<Path> getAllFiles(@PathVariable String id) {
        ScavengerService scavengerService = new ScavengerService();

        List<Path> listOfFiles;

        listOfFiles = scavengerService.findAll(repository.findById(id).getPath());

        return listOfFiles;
    }

    @GetMapping("/{id}/biggestfiles")
    public List<Path> getBiggestFiles(@PathVariable String id) {
        AnalyzerService analyzerService = new AnalyzerService();

        ScavengerService scavengerService = new ScavengerService();

        List<Path> listOfFiles;

        listOfFiles = scavengerService.findAll(repository.findById(id).getPath());

        return analyzerService.getBiggestFiles(listOfFiles, 10);
    }

    @GetMapping("/{id}/dublicates")
    public Set<Path> getDuplicates(@PathVariable String id) throws NoSuchAlgorithmException, IOException {
        AnalyzerService analyzerService = new AnalyzerService();

        ScavengerService scavengerService = new ScavengerService();

        List<Path> listOfFiles;

        listOfFiles = scavengerService.findAll(repository.findById(id).getPath());

        return analyzerService.getDuplicates(listOfFiles);
    }

    @GetMapping("/{id}/unknownfiles")
    public Map<Path, String> getUnknownFiles(@PathVariable String id) throws NoSuchAlgorithmException, IOException {
        AnalyzerService analyzerService = new AnalyzerService();

        ScavengerService scavengerService = new ScavengerService();

        List<Path> listOfFiles;

        listOfFiles = scavengerService.findAll(repository.findById(id).getPath());

        return analyzerService.getUnknownFiles(listOfFiles);
    }
}
