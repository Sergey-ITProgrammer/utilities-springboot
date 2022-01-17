package com.utilities.domain;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ScannedObject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "path")
    @NonNull
    private String path = "";

    public ScannedObject() {}

    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
