package com.utilities.domain;

import org.springframework.lang.NonNull;

import java.util.UUID;

public class ScannedObject {
    @NonNull
    private String path;
    private String id;

    public ScannedObject() {
        this.id = UUID.randomUUID().toString();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getId() {
        return id;
    }
}
