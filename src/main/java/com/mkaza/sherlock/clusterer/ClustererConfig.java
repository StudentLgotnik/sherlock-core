package com.mkaza.sherlock.clusterer;

import org.apache.commons.math3.ml.distance.DistanceMeasure;

public class ClustererConfig {

    private double epsilon;

    private int minPts;

    private DistanceMeasure distanceMeasure;

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public int getMinPts() {
        return minPts;
    }

    public void setMinPts(int minPts) {
        this.minPts = minPts;
    }

    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }
}
