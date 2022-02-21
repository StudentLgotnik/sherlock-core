package com.mkaza.sherlock.clusterer;

import com.mkaza.sherlock.clusterer.impl.DBSCANSherlockClusterer;
import com.mkaza.sherlock.clusterer.impl.DBSCANParameters;
import com.mkaza.sherlock.model.ClusterableTestCase;

public class SherlockClustererFactory {

    public static SherlockClusterer<ClusterableTestCase> createClusterer(ClusteringAlgorithm clusteringAlgorithm) {

        switch (clusteringAlgorithm.getAlgorithm()) {
            case DBSCAN:
                return new DBSCANSherlockClusterer<>(
                        ((ParametrizedClusteringAlgorithm<?>) clusteringAlgorithm).getParameters()
                );
            default:
                return defaultClusterer();
        }
    }

    private static SherlockClusterer<ClusterableTestCase> defaultClusterer() {
        return new DBSCANSherlockClusterer<>(DBSCANParameters.builder().build());
    }
}
