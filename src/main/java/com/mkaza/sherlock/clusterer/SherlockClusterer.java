package com.mkaza.sherlock.clusterer;

import com.mkaza.sherlock.util.DbscanUtil;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SherlockClusterer<T extends Clusterable> {

    public List<Cluster<T>> cluster(List<T> rescaledDataRows) {
        return cluster(rescaledDataRows, ClustererConfig.builder().build());
    }

    public List<Cluster<T>> cluster(List<T> rescaledDataRows, ClustererConfig config) {

        if (rescaledDataRows == null) {
            throw new NullArgumentException();
        }

        int minPts = config.getMinPts().orElseGet(() -> DbscanUtil.calcMinPts(rescaledDataRows.size()));
        double epsilon = config.getEpsilon().orElseGet(() -> DbscanUtil.calcAverageEpsilon(rescaledDataRows, minPts));
        double epsilon2 = config.getEpsilon().orElseGet(() -> DbscanUtil.calcEpsilon(rescaledDataRows, minPts));
        DistanceMeasure distanceMeasure = config.getDistanceMeasure().orElseGet(EuclideanDistance::new);

        final DBSCANClusterer<T> transformer = new DBSCANClusterer<>(epsilon, minPts, distanceMeasure);

        List<Cluster<T>> result = transformer.cluster(rescaledDataRows);

        if (!config.isExcludeNoiseNodes()) {
            addRemainingTestCases(result, rescaledDataRows);
        }

        return result;
    }

    private void addRemainingTestCases(List<Cluster<T>> clusters, List<T> remaining) {
        List<T> existing = clusters.stream().flatMap(c -> c.getPoints().stream()).collect(Collectors.toList());
        remaining.stream()
                .filter(r -> !existing.contains(r))
                .forEach(r -> {
                    Cluster<T> cluster = new Cluster<>();
                    cluster.addPoint(r);
                    clusters.add(cluster);
        });
    }

}
