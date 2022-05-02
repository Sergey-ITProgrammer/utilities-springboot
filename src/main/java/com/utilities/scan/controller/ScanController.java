package com.utilities.scan.controller;

import com.utilities.domain.ScannedObject;
import com.utilities.scan.service.ScanService;
import com.utilities.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/utilities/scan")
public class ScanController {

    private final ScanService scanService;

    @Autowired
    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    @PostMapping("")
    public ResponseEntity<?> createScannedObject(@RequestBody ScannedObject object) {
        if (object.getPath().isEmpty()) {
            return new ResponseEntity<>("Path is empty", HttpStatus.BAD_REQUEST);
        }

        scanService.createScannedObject(object);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(object.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("")
    public ResponseEntity<?> changeScannedObject(@RequestParam(value = "id") long id, @RequestParam(value = "path") String path) {
        ScannedObject scannedObject = scanService.changeScannedObject(id, path);

        if (scannedObject == null) {
            return new ResponseEntity<>("The object doesn't exist", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok("The scanned object has been changed successfully\n");
    }

    @GetMapping("")
    public ResponseEntity<?> getAllScannedObjects() {
        return ResponseEntity.ok(scanService.getAllScannedObjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllFiles(@PathVariable long id) {
        List<String> allFiles = scanService.getAllFiles(id);

        if (allFiles == null) {
            return new ResponseEntity<>("Object doesn't exist", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(allFiles);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteScannedObject(@RequestParam Long id) {

        scanService.deleteScannedObject(id);

        return ResponseEntity.ok("The scanned object has been deleted successfully\n");
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationError handleException(Exception exception) {
        return new ValidationError(exception.getMessage());
    }
}