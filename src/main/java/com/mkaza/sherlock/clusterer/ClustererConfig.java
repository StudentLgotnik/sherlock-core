package com.mkaza.sherlock.clusterer;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

@Builder
@Getter
public class ClustererConfig {

    private Double epsilon;

    private Integer minPts;

    private DistanceMeasure distanceMeasure;

}
