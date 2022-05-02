package com.utilities.analysis.controller;


import com.utilities.analysis.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/utilities/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @GetMapping("/{id}/biggest")
    public ResponseEntity<?> getBiggestFiles(@PathVariable long id, @RequestParam int amount) {
        List<Path> biggestFiles = analysisService.getBiggestFiles(id, amount);

        if (biggestFiles == null) {
            return new ResponseEntity<>("Object doesn't exist", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(biggestFiles);
    }

    @GetMapping("/{id}/duplicates")
    public ResponseEntity<?> getDuplicates(@PathVariable long id) {
        Set<Path> duplicates = analysisService.getDuplicates(id);

        if (duplicates == null) {
            return new ResponseEntity<>("Object doesn't exist", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(duplicates);
    }

    @GetMapping("/{id}/unknown")
    public ResponseEntity<?> getUnknownFiles(@PathVariable long id) {
        Map<Path, String> unknownFiles = analysisService.getUnknownFiles(id);

        if (unknownFiles == null) {
            return new ResponseEntity<>("Object doesn't exist", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(unknownFiles);
    }
}
