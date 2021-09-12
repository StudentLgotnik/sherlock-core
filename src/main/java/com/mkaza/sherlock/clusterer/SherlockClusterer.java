package com.mkaza.sherlock.clusterer;

import com.mkaza.sherlock.util.DbscanUtil;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.List;

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
        DistanceMeasure distanceMeasure = config.getDistanceMeasure().orElseGet(EuclideanDistance::new);

        final DBSCANClusterer<T> transformer = new DBSCANClusterer<>(epsilon, minPts, distanceMeasure);

        return transformer.cluster(rescaledDataRows);
    }
}
