package com.mkaza.sherlock.parser.provider.impl;

import com.mkaza.sherlock.parser.provider.LogsProvider;
import com.mkaza.sherlock.parser.suplier.FileLogSupplier;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirLogsProvider implements LogsProvider {

    private static final Logger logger = Logger.getLogger(DirLogsProvider.class);

    private final String pathToDir;

    public DirLogsProvider(String pathToDir) {
        this.pathToDir = pathToDir;
    }

    @Override
    public List<Supplier<InputStream>> getLogs() {
        try (Stream<Path> paths = Files.walk(Paths.get(pathToDir))) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(p -> new FileLogSupplier(p.toString()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error(String.format("Couldn't read from the directory %s!", pathToDir), e);
        }
        return Collections.emptyList();
    }
}
