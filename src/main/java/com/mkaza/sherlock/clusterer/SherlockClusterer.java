package com.mkaza.sherlock.clusterer;

import com.mkaza.sherlock.model.ClusterableRow;
import com.mkaza.sherlock.util.DbscanUtil;
import com.mkaza.sherlock.util.GenericBuilder;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.Collection;
import java.util.List;

public class SherlockClusterer {

    private final ClustererConfig clustererConfig;


    public SherlockClusterer(ClustererConfig clustererConfig) {
        this.clustererConfig = clustererConfig;
    }

    public SherlockClusterer() {
        clustererConfig = GenericBuilder.of(ClustererConfig::new)
                .with(ClustererConfig::setEpsilon, 3.5)
                .with(ClustererConfig::setMinPts, 5)
                .with(ClustererConfig::setDistanceMeasure, new EuclideanDistance())
                .build();
    }

    public List<Cluster<ClusterableRow>> cluster(List<ClusterableRow> rescaledDataRows) {

        int minPts = DbscanUtil.calcMinPts(rescaledDataRows.size());
        double epsilon = DbscanUtil.calcAverageEpsilon(rescaledDataRows, minPts);

        ClustererConfig clustererConfig = GenericBuilder.of(ClustererConfig::new)
                .with(ClustererConfig::setEpsilon, epsilon)
                .with(ClustererConfig::setMinPts, minPts)
                .with(ClustererConfig::setDistanceMeasure, new EuclideanDistance())
                .build();

        return cluster(rescaledDataRows, clustererConfig);
    }

    public List<Cluster<ClusterableRow>> cluster(Collection<ClusterableRow> rescaledDataRows, ClustererConfig config) {

        final DBSCANClusterer<ClusterableRow> transformer =
                new DBSCANClusterer<>(config.getEpsilon(), config.getMinPts(), config.getDistanceMeasure());

        return transformer.cluster(rescaledDataRows);
    }
}
