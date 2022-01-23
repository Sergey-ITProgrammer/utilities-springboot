package com.utilities.repository;

import com.utilities.domain.FilePath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryOfFilesPath extends JpaRepository<FilePath, Long> {

    List<FilePath> findByObjectId(long id);

    @Modifying
    void deleteByObjectId(long id);
}
