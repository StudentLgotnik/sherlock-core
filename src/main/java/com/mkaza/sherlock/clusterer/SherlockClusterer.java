package com.mkaza.sherlock.clusterer;

import com.mkaza.sherlock.util.DbscanUtil;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.Collection;
import java.util.List;

public class SherlockClusterer<T extends Clusterable> {

    public List<Cluster<T>> cluster(List<T> rescaledDataRows) {

        int minPts = DbscanUtil.calcMinPts(rescaledDataRows.size());
        double epsilon = DbscanUtil.calcAverageEpsilon(rescaledDataRows, minPts);

        ClustererConfig clustererConfig = ClustererConfig.builder()
                .epsilon(epsilon)
                .minPts(minPts)
                .distanceMeasure(new EuclideanDistance())
                .build();

        return cluster(rescaledDataRows, clustererConfig);
    }

    public List<Cluster<T>> cluster(Collection<T> rescaledDataRows, ClustererConfig config) {

        final DBSCANClusterer<T> transformer =
                new DBSCANClusterer<>(config.getEpsilon(), config.getMinPts(), config.getDistanceMeasure());

        return transformer.cluster(rescaledDataRows);
    }
}
