package com.mkaza.sherlock.model;

import org.apache.commons.math3.ml.clustering.Clusterable;

//For test purposes
public class ClusterableDataPoint implements Clusterable {

    private final double[] point;

    public ClusterableDataPoint(double[] point) {
        this.point = point;
    }

    @Override
    public double[] getPoint() {
        return point;
    }
}
