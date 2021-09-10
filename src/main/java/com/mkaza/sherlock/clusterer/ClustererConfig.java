package com.mkaza.sherlock.clusterer;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

@Builder
@Getter
public class ClustererConfig {

    private double epsilon;

    private int minPts;

    @NonNull
    private DistanceMeasure distanceMeasure;

}
