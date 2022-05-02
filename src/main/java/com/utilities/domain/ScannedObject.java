package com.utilities.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@Entity
public class ScannedObject {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Getter
    @Setter
    @NonNull
    private String path = "";

    @Getter
    @Setter
    @OneToMany(mappedBy = "scannedObject", cascade = {CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection<FilePath> allFilesList = new ArrayList<>();
}
