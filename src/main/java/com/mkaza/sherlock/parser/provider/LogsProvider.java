package com.mkaza.sherlock.parser.provider;

import java.io.InputStream;
import java.util.List;
import java.util.function.Supplier;

public interface LogsProvider {

    /**
     * Provides a data source.
     * @return list of sources from which data can be read
     */
    List<Supplier<InputStream>> getLogs();

}
