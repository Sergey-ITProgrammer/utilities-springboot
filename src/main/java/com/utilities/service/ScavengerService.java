package com.utilities.service;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ScavengerService {
    private List<Path> list = new ArrayList<>();

    public List<Path> findAll(String path) {
        if (!list.isEmpty()) return list;

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

    public void cleanList() {
        list = new ArrayList<>();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
