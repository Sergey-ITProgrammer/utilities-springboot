package com.utilities.controller;


import com.utilities.domain.ScannedObject;
import com.utilities.service.AnalyzerService;
import com.utilities.service.ScavengerService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/utilities")
public class MainController {
    ScannedObject pathScannedObject = new ScannedObject();

    ScavengerService scavengerService = new ScavengerService();
    AnalyzerService analyzerService = new AnalyzerService();

    private List<Path> listOfFiles;

    @PostMapping("")
    public void setPathAndFindAllFiles(@RequestBody String path) {
        pathScannedObject.setPath(path);

        listOfFiles = scavengerService.findAll(pathScannedObject.getPath());
    }

    @GetMapping("")
    public List<Path> getAllFiles() {
        return listOfFiles;
    }

    @GetMapping("/biggestfiles")
    public List<Path> getBiggestFiles() {
        return analyzerService.getBiggestFiles(listOfFiles, 10);
    }

    @GetMapping("/dublicates")
    public Set<Path> getDuplicates() throws NoSuchAlgorithmException, IOException {
        return analyzerService.getDuplicates(listOfFiles);
    }

    @GetMapping("/unknownfiles")
    public Map<Path, String> getUnknownFiles() throws NoSuchAlgorithmException, IOException {
        return analyzerService.getUnknownFiles(listOfFiles);
    }
}
