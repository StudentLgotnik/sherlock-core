package com.mkaza.sherlock.clusterer;

import com.mkaza.sherlock.util.DbscanUtil;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.List;
import java.util.Objects;

public class SherlockClusterer<T extends Clusterable> {

    public List<Cluster<T>> cluster(List<T> rescaledDataRows) {
        return cluster(rescaledDataRows, ClustererConfig.builder().build());
    }

    public List<Cluster<T>> cluster(List<T> rescaledDataRows, ClustererConfig config) {

        int minPts = Objects.nonNull(config.getMinPts()) ? config.getMinPts() : DbscanUtil.calcMinPts(rescaledDataRows.size());
        double epsilon = Objects.nonNull(config.getEpsilon()) ? config.getEpsilon() : DbscanUtil.calcAverageEpsilon(rescaledDataRows, minPts);
        DistanceMeasure distanceMeasure = Objects.nonNull(config.getDistanceMeasure()) ? config.getDistanceMeasure() : new EuclideanDistance();

        final DBSCANClusterer<T> transformer = new DBSCANClusterer<>(epsilon, minPts, distanceMeasure);

        return transformer.cluster(rescaledDataRows);
    }
}
