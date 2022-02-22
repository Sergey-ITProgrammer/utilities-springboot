package com.utilities.scan.service;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ScavengerServiceImpl implements ScavengerService {

    private List<Path> list;

    @Override
    public List<Path> findAll(String path) {
        list = new ArrayList<>();

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
