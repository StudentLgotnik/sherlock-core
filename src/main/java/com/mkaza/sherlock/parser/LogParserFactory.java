package com.mkaza.sherlock.parser;

import com.mkaza.sherlock.parser.impl.XmlLogParser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class LogParserFactory {

    final static Map<String, Supplier<LogParser>> map = new HashMap<>();

    static {
        map.put(LogParserType.XML.name(), XmlLogParser::new);
        map.put(LogParserType.DEFAULT.name(), XmlLogParser::new);
    }

    public static LogParser getParser(LogParserType parserType){
        Supplier<LogParser> shape = map.get(parserType.name());
        if(shape != null) {
            return shape.get();
        }
        throw new IllegalArgumentException("No such parser " + parserType.name());
    }
}
