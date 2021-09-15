package com.mkaza.sherlock.parser;

import java.util.Map;

public interface LogParser {

    /**
     * Parse logs from provided file
     * @param logFilePath path to file with logs
     * @return batch of test case and related errors from logs
     */
    Map<String, String> parse(String logFilePath);
}
