package com.mkaza.sherlock.parser;

import java.io.File;
import java.util.List;

public interface LogParser {

    /**
     * Parse logs from provided path directory
     * @param logsFilesDirPath path to directory with logs
     * @return batch of parse errors from logs
     */
    List<String> parseDir(String logsFilesDirPath);

    /**
     * Parse logs from provided file
     * @param logsFilesPath file with logs
     * @return batch of parse errors from logs
     */
    List<String> parseFile(String logsFilesPath);
}
