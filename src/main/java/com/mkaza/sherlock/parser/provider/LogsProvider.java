package com.mkaza.sherlock.parser.provider;

import java.io.InputStream;
import java.util.List;
import java.util.function.Supplier;

public interface LogsProvider {

    List<Supplier<InputStream>> getLogs();

}
