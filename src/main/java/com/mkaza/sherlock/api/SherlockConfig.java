package com.mkaza.sherlock.api;

import com.mkaza.sherlock.clusterer.ClustererConfig;
import com.mkaza.sherlock.parser.LogParser;
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
    private String pathToLogs;

    /**
     * Clusterer configuration model to override suggested one.
     */
    private ClustererConfig clustererConfig;

    public static SherlockConfigBuilder builder(String logFilePath) {
        return hiddenBuilder().pathToLogs(logFilePath);
    }
}
