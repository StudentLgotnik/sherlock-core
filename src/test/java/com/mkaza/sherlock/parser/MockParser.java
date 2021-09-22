package com.mkaza.sherlock.parser;

import com.mkaza.sherlock.parser.provider.LogsProvider;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MockParser implements LogParser {

    @Override
    public Map<String, String> parse(LogsProvider logsFilesPath) {
        try {
            String[] lines = new String(logsFilesPath.getLogs().get(0).get().readAllBytes(), StandardCharsets.UTF_8).split("\\r\\n");
            return IntStream
                    .range(0, lines.length)
                    .boxed()
                    .collect(Collectors.toMap(String::valueOf, i -> lines[i]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }
}
