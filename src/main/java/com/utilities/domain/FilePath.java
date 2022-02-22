package com.utilities.domain;

import javax.persistence.*;

@Entity
public class FilePath {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ScannedObject scannedObject;

    public FilePath() {}

    public FilePath(String path, ScannedObject scannedObject) {
        this.path = path;
        this.scannedObject = scannedObject;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
