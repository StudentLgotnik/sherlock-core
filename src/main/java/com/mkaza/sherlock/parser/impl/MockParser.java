package com.mkaza.sherlock.parser.impl;

import com.mkaza.sherlock.parser.LogParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MockParser implements LogParser {

    @Override
    public Map<String, String> parse(String logsFilesPath) {
        try {
            String[] lines = Files.readString(Paths.get(logsFilesPath)).split("\\r\\n");
            return IntStream
                    .range(0, lines.length)
                    .boxed()
                    .collect(Collectors.toMap(i -> logsFilesPath + i, i -> lines[i]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }
}
