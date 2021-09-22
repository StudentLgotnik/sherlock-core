package com.mkaza.sherlock.parser.provider.impl;

import com.mkaza.sherlock.parser.suplier.FileLogSupplier;
import com.mkaza.sherlock.parser.provider.LogsProvider;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class FileLogsProvider implements LogsProvider {

    private final String pathToFile;

    public FileLogsProvider(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    @Override
    public List<Supplier<InputStream>> getLogs() {
        return Collections.singletonList(new FileLogSupplier(pathToFile));
    }
}
