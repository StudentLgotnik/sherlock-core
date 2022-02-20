package com.mkaza.sherlock.clusterer;

import com.mkaza.sherlock.clusterer.impl.DBSCANSherlockClusterer;
import com.mkaza.sherlock.model.ClusterableTestCase;

import java.util.Objects;

public class SherlockClustererFactory {

    public static SherlockClusterer<ClusterableTestCase> createClusterer(SherlockClustererConfig config) {
        if (Objects.isNull(config)) {
            throw new IllegalArgumentException("");
        }
        switch (config.getClusteringAlgorithm()) {
            case DBSCAN:
                return Objects.nonNull(config.getConfig())
                        ? new DBSCANSherlockClusterer<>(config.getConfig())
                        : new DBSCANSherlockClusterer<>();
            default:
                return Objects.nonNull(config.getConfig())
                        ? new DBSCANSherlockClusterer<>(config.getConfig())
                        : new DBSCANSherlockClusterer<>();
        }
    }
}
