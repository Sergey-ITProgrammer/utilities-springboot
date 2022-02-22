package com.utilities.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class ScannedObject {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @NonNull
    private String path = "";

    @OneToMany(mappedBy = "scannedObject", cascade = {CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection<FilePath> allFilesList = new ArrayList<>();

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

    public void setAllFilesList(List<FilePath> list) {
        this.allFilesList = list;
    }

    public Collection<FilePath> getAllFilesList() {
        return allFilesList;
    }
}
