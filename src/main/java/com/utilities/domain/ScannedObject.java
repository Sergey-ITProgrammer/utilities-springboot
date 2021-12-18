package com.utilities.domain;

import org.springframework.lang.NonNull;

public class ScannedObject {
    @NonNull
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
