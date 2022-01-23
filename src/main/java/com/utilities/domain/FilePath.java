package com.utilities.domain;

import javax.persistence.*;
import java.nio.file.Path;

@Entity
@Table(name = "path")
public class FilePath {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scannedObject_id")
    private ScannedObject object;

    public FilePath() {
    }

    public FilePath(Path path) {
        this.path = path.toString();
    }

    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setObject(ScannedObject object) {
        this.object = object;
    }
}
