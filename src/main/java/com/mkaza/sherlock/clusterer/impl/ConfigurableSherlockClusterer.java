package com.mkaza.sherlock.clusterer.impl;

import com.mkaza.sherlock.clusterer.SherlockClusterer;
import com.mkaza.sherlock.clusterer.SherlockClustererConfig;
import org.apache.commons.math3.ml.clustering.Clusterable;

public abstract class ConfigurableSherlockClusterer<T extends Clusterable, E extends SherlockClustererConfig> implements SherlockClusterer<T> {

    protected E config;

    public ConfigurableSherlockClusterer(E config) {
        this.config = config;
    }
}
