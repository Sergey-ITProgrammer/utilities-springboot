package com.utilities.repository;

import com.utilities.domain.ScannedObject;
import org.springframework.data.repository.CrudRepository;

public interface RepositoryOfScannedObject extends CrudRepository<ScannedObject, Long> {

}
