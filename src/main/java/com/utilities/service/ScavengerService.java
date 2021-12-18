package com.utilities.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

@Service
public class ScavengerService {
    final private List<Path> list = new ArrayList<>();

    public List<Path> findAll(String path) {
        File dir = new File(String.valueOf(path));

        searchFiles(dir);

        return list;
    }

    private void searchFiles(File dir) {
        if(dir.isDirectory()) {
            for(File item : Objects.requireNonNull(dir.listFiles())) {
                if (item.isDirectory()) {
                    searchFiles(item);
                } else {
                    list.add(item.toPath());
                }
            }
        }
    }
}
