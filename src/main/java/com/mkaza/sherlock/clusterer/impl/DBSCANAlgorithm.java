package com.mkaza.sherlock.clusterer.impl;

import com.mkaza.sherlock.clusterer.SherlockClusteringAlgorithm;
import com.mkaza.sherlock.clusterer.ParametrizedClusteringAlgorithm;

public class DBSCANAlgorithm extends ParametrizedClusteringAlgorithm<DBSCANParameters> {

    public DBSCANAlgorithm(DBSCANParameters parameters) {
        super(parameters);
    }

    public DBSCANAlgorithm() {
        super(DBSCANParameters.builder().build());
    }

    @Override
    public SherlockClusteringAlgorithm getAlgorithm() {
        return SherlockClusteringAlgorithm.DBSCAN;
    }
}
