package com.mkaza.sherlock.api;

import com.mkaza.sherlock.clusterer.ClustererConfig;
import com.mkaza.sherlock.parser.LogParser;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder(builderMethodName = "hiddenBuilder")
@Getter
public class SherlockConfig {

    private LogParser parser;

    @NonNull
    private String logFilePath;

    private ClustererConfig clustererConfig;

    public static SherlockConfigBuilder builder(String logFilePath) {
        return hiddenBuilder().logFilePath(logFilePath);
    }
}
