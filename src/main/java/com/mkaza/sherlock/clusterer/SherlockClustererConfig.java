package com.mkaza.sherlock.clusterer;

public interface SherlockClustererConfig {

    SherlockClusteringAlgorithm getClusteringAlgorithm();

    <E> E getConfig();

}
