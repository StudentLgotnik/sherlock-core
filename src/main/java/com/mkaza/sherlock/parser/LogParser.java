package com.mkaza.sherlock.parser;

import com.mkaza.sherlock.parser.provider.LogsProvider;

import java.util.Map;

public interface LogParser {

    /**
     * Parse logs from provided file
     * @param logsProvider path to file with logs
     * @return batch of test case and related errors from logs
     */
    Map<String, String> parse(LogsProvider logsProvider);
}
