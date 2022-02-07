package com.utilities.domain;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
public class ScannedObject {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "path")
    @NonNull
    private String path = "";

    @ElementCollection
    @Column(name = "FILE_PATH")
    private List<String> allFilesList;

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

    public void setAllFilesList(List<String> list) {
        this.allFilesList = list;
    }

    public List<String> getAllFilesList() {
        return allFilesList;
    }
}
