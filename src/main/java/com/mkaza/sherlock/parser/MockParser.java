package com.mkaza.sherlock.parser;

import com.mkaza.sherlock.ContentVectorizerExample;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MockParser implements LogParser{


    @Override
    public List<String> parseDir(String logsFilesDirPath) {
        return null;
    }

    @Override
    public List<String> parseFile(String logsFilesPath) {
        try {
            return List.of(Files.readString(Paths.get(logsFilesPath)).split("\\r\\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
