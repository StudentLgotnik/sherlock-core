package com.mkaza.sherlock.model;

import org.apache.commons.math3.ml.clustering.Cluster;

import java.util.List;

public class TestCaseCluster {

    private final Cluster<ClusterableTestCase> cluster;

    public TestCaseCluster(Cluster<ClusterableTestCase> cluster) {
        this.cluster = cluster;
    }

    public List<? extends TestCase> getCases() {
        return cluster.getPoints();
    }
}
