package com.mkaza.sherlock.clusterer;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;

import java.util.List;

public interface SherlockClusterer<T extends Clusterable> {

    /**
     *
     * @param rescaledDataRows
     * @return
     */
    List<Cluster<T>> cluster(List<T> rescaledDataRows);

}
