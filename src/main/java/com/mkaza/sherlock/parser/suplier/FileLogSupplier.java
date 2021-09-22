package com.mkaza.sherlock.parser.suplier;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.function.Supplier;

public class FileLogSupplier implements Supplier<InputStream> {

    private static final Logger logger = Logger.getLogger(FileLogSupplier.class);

    private final String pathToFile;

    public FileLogSupplier(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    @Override
    public InputStream get() {
        try {
            return new FileInputStream(pathToFile);
        } catch (FileNotFoundException e) {
            logger.error(String.format("Path to file is invalid: %s", pathToFile), e);
        }
        return InputStream.nullInputStream();
    }
}
