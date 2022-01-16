package com.utilities.scan.controller;

import com.utilities.domain.ScannedObject;
import com.utilities.scan.service.ScanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/utilities/scan")
public class ScanController {
    private final ScanService scanService;

    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    @PostMapping("")
    public ResponseEntity<?> createScannedObject(@RequestBody ScannedObject object, HttpServletRequest httpServletRequest) {
        scanService.createScannedObject(object);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(object.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("")
    public ResponseEntity<?> changeScannedObject(@RequestParam(value = "id") String id, @RequestParam(value = "path") String path) {
        if (id.isEmpty() || path.isEmpty()) {
            return new ResponseEntity<>("Id or path is empty!", HttpStatus.NO_CONTENT);
        }

        scanService.changeScannedObject(id, path);

        return ResponseEntity.ok("The scanned object has been changed successfully\n");
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteScannedObject(@RequestParam String id) {
        if (id.isEmpty()) {
            return new ResponseEntity<>("Id is empty!", HttpStatus.NO_CONTENT);
        }

        scanService.deleteScannedObject(id);

        return ResponseEntity.ok("The scanned object has been deleted successfully\n");
    }

    @GetMapping("")
    public ResponseEntity<?> getAllScannedObjects() {
        Map<String, ScannedObject> allScannedObjects = scanService.getAllScannedObjects();

        if (allScannedObjects == null || allScannedObjects.isEmpty()) {
            return new ResponseEntity<>("List is empty", HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(allScannedObjects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllFiles(@PathVariable String id) {
        List<Path> allFiles = scanService.getAllFiles(id);

        if (allFiles == null || allFiles.isEmpty()) {
            return new ResponseEntity<>("List is empty", HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(allFiles);
    }
}