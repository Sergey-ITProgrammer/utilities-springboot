package com.utilities.repository;

import com.utilities.domain.ScannedObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryOfScannedObject extends JpaRepository<ScannedObject, Long> {

}
