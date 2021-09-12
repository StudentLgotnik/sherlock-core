package com.mkaza.sherlock.clusterer;

import lombok.Builder;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import java.util.Optional;

@Builder
public class ClustererConfig {

    private Double epsilon;

    private Integer minPts;

    private DistanceMeasure distanceMeasure;

    public Optional<Double> getEpsilon() {
        return Optional.ofNullable(epsilon);
    }

    public Optional<Integer> getMinPts() {
        return Optional.ofNullable(minPts);
    }

    public Optional<DistanceMeasure> getDistanceMeasure() {
        return Optional.ofNullable(distanceMeasure);
    }
}
