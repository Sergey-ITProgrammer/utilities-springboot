package com.utilities.scan.service;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Christian Luzzetti
 * @created 2021/11/25 - 20:26
 */

public interface ScavengerService {

    /***
     *
     * @return a List of all the regular files that have been found
     */
    List<Path> findAll(String path);
}
