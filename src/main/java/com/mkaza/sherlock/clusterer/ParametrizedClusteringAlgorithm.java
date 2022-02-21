package com.mkaza.sherlock.clusterer;

import java.util.Objects;

public abstract class ParametrizedClusteringAlgorithm<T extends InitialParameters> implements ClusteringAlgorithm {

    private final T parameters;

    public ParametrizedClusteringAlgorithm(T parameters) {
        if (Objects.isNull(parameters)) {
            throw new IllegalArgumentException(String.format("%s requires initial parameters!", getAlgorithm()));
        }
        this.parameters = parameters;
    }

    public <E> E getParameters() {
        return (E) parameters;
    }
}
