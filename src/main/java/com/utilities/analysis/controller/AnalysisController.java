package com.utilities.analysis.controller;


import com.utilities.analysis.service.AnalysisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/utilities/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/{id}/biggestfiles")
    public ResponseEntity<?> getBiggestFiles(@PathVariable String id, @RequestParam int amount) {
        List<Path> biggestFiles = analysisService.getBiggestFiles(id, amount);

        if (biggestFiles == null || biggestFiles.isEmpty()) {
            return new ResponseEntity<>("List is empty", HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(biggestFiles);
    }

    @GetMapping("/{id}/duplicates")
    public ResponseEntity<?> getDuplicates(@PathVariable String id) throws NoSuchAlgorithmException, IOException {
        Set<Path> duplicates = analysisService.getDuplicates(id);

        if (duplicates == null || duplicates.isEmpty()) {
            return new ResponseEntity<>("List is empty", HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(duplicates);
    }

    @GetMapping("/{id}/unknownfiles")
    public ResponseEntity<?> getUnknownFiles(@PathVariable String id) throws NoSuchAlgorithmException, IOException {
        Map<Path, String> unknownFiles = analysisService.getUnknownFiles(id);

        if (unknownFiles == null || unknownFiles.isEmpty()) {
            return new ResponseEntity<>("List is empty", HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(unknownFiles);
    }
}
