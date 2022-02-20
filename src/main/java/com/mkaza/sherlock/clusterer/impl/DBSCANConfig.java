package com.mkaza.sherlock.clusterer.impl;

import com.mkaza.sherlock.clusterer.SherlockClustererConfig;
import com.mkaza.sherlock.clusterer.SherlockClusteringAlgorithm;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import java.util.Optional;

@Builder
public class DBSCANConfig implements SherlockClustererConfig {

    private Double epsilon;

    private Integer minPts;

    private DistanceMeasure distanceMeasure;

    @Getter
    private boolean excludeNoiseNodes;

    public Optional<Double> getEpsilon() {
        return Optional.ofNullable(epsilon);
    }

    public Optional<Integer> getMinPts() {
        return Optional.ofNullable(minPts);
    }

    public Optional<DistanceMeasure> getDistanceMeasure() {
        return Optional.ofNullable(distanceMeasure);
    }

    @Override
    public SherlockClusteringAlgorithm getClusteringAlgorithm() {
        return SherlockClusteringAlgorithm.DBSCAN;
    }

    @Override
    public <E> E getConfig() {
        return (E) this;
    }
}
