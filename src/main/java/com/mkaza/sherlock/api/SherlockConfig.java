package com.mkaza.sherlock.api;

import com.mkaza.sherlock.clusterer.ClusteringAlgorithm;
import com.mkaza.sherlock.parser.LogParser;
import com.mkaza.sherlock.parser.provider.LogsProvider;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder(builderMethodName = "hiddenBuilder")
@Getter
public class SherlockConfig {

    /**
     * Own parser realization which will be used for parsing logs under logsProvider data source.
     */
    private LogParser parser;

    /**
     * Could be as path to single file or as path to directory with logs or own realization.
     */
    @NonNull
    private LogsProvider logsProvider;

    /**
     * Clustering algorithm which will be used for clustering logs.
     */
    @NonNull
    private ClusteringAlgorithm clusteringAlgorithm;

    public static SherlockConfigBuilder builder(LogsProvider logsProvider, ClusteringAlgorithm clusteringAlgorithm) {
        return hiddenBuilder()
                .logsProvider(logsProvider)
                .clusteringAlgorithm(clusteringAlgorithm);
    }
}
