package com.mkaza.sherlock.api;

import com.mkaza.sherlock.clusterer.SherlockClustererConfig;
import com.mkaza.sherlock.parser.LogParser;
import com.mkaza.sherlock.parser.provider.LogsProvider;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder(builderMethodName = "hiddenBuilder")
@Getter
public class SherlockConfig {

    /**
     * Own parser realization which will be used for parsing logs under pathToLogs path.
     */
    private LogParser parser;

    /**
     * Could be as path to single file or as path to directory with logs.
     */
    @NonNull
    private LogsProvider logsProvider;

    /**
     * Clusterer configuration model to override suggested one.
     */
    private SherlockClustererConfig clustererConfig;

    public static SherlockConfigBuilder builder(LogsProvider logsProvider) {
        return hiddenBuilder().logsProvider(logsProvider);
    }
}
