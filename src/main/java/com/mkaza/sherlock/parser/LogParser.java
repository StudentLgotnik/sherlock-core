package com.mkaza.sherlock.parser;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface LogParser {

    /**
     * Parse logs from provided file
     * @param logsFilesPath path to file with logs
     * @return batch of test case and related errors from logs
     */
    Map<String, String> parse(String logsFilesPath);
}
