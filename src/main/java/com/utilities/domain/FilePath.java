package com.utilities.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class FilePath {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @Setter
    @Getter
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ScannedObject scannedObject;

    public FilePath(String path, ScannedObject scannedObject) {
        this.path = path;
        this.scannedObject = scannedObject;
    }
}
