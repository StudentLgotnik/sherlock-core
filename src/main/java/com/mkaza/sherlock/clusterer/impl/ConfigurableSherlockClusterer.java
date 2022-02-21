package com.mkaza.sherlock.clusterer.impl;

import com.mkaza.sherlock.clusterer.SherlockClusterer;
import com.mkaza.sherlock.clusterer.InitialParameters;
import org.apache.commons.math3.ml.clustering.Clusterable;

public abstract class ConfigurableSherlockClusterer<T extends Clusterable, E extends InitialParameters> implements SherlockClusterer<T> {

    private final E config;

    public ConfigurableSherlockClusterer(E config) {
        this.config = config;
    }

    public E getConfig() {
        return config;
    }
}
