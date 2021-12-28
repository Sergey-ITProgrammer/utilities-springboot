package com.utilities.controller;


import com.utilities.domain.ScannedObject;
import com.utilities.service.UtilitiesService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@RequestMapping("/utilities")
public class UtilitiesController {
    private UtilitiesService utilitiesService;

    public UtilitiesController(UtilitiesService utilitiesService) {
        this.utilitiesService = utilitiesService;
    }

    @PostMapping("")
    public void createScannedObject(@RequestBody ScannedObject object) {
        System.out.println(object.getPath());
        System.out.println(object.getId());

        utilitiesService.createScannedObject(object);
    }

    @PutMapping("{id}")
    public void changeScannedObject(@PathVariable String id, @RequestParam String newPath) {
        utilitiesService.changeScannedObject(id, newPath);
    }

    @DeleteMapping("{id}")
    public void deleteScannedObject(@PathVariable String id) {
        utilitiesService.deleteScannedObject(id);
    }

    @GetMapping("")
    public Map<String, ScannedObject> getAllScannedObjects() {
        return utilitiesService.getAllScannedObjects();
    }

    @GetMapping("/{id}")
    public List<Path> getAllFiles(@PathVariable String id) {
        return utilitiesService.getAllFiles(id);
    }

    @GetMapping("/{id}/biggestfiles/{amountOfFiles}")
    public List<Path> getBiggestFiles(@PathVariable String id, @PathVariable int amountOfFiles) {
        return utilitiesService.getBiggestFiles(id, amountOfFiles);
    }

    @GetMapping("/{id}/duplicates")
    public Set<Path> getDuplicates(@PathVariable String id) throws NoSuchAlgorithmException, IOException {
        return utilitiesService.getDuplicates(id);
    }

    @GetMapping("/{id}/unknownfiles")
    public Map<Path, String> getUnknownFiles(@PathVariable String id) throws NoSuchAlgorithmException, IOException {
        return utilitiesService.getUnknownFiles(id);
    }
}
