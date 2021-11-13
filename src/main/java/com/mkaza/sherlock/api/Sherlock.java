package com.mkaza.sherlock.api;

import java.util.List;

public interface Sherlock<T> {

    /**
     * Forms clusters of the specified data type based on the provided logs.
     * @return List of clusters
     */
    List<T> cluster();

}
