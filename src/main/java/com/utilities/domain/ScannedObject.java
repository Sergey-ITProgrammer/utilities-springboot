package com.utilities.domain;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
public class ScannedObject {

    @Id
    @GeneratedValue
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
