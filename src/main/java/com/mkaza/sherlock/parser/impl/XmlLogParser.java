package com.mkaza.sherlock.parser.impl;

import com.google.common.base.Strings;
import com.mkaza.sherlock.parser.LogParser;
import com.mkaza.sherlock.parser.dto.SurefirePluginXmlDto;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class XmlLogParser implements LogParser {

    private static final Logger logger = LogManager.getLogger(XmlLogParser.class);

    @Override
    public Map<String, String> parse(String logFilePath) {

        try(FileReader reader = new FileReader(logFilePath)) {

            JAXBContext context = JAXBContext.newInstance(SurefirePluginXmlDto.class);

            SurefirePluginXmlDto surefirePluginXmlDto = (SurefirePluginXmlDto) context
                    .createUnmarshaller()
                    .unmarshal(reader);

            logger.info("Successfully parsed logfile {}!", Paths.get(logFilePath).getFileName());

            return surefirePluginXmlDto.getTestCases().stream()
                    .filter(tc -> !Strings.isNullOrEmpty(tc.getStackTrace()))
                    .collect(Collectors.toMap(
                            tc -> tc.getClassname() + "." + tc.getName(),
                            SurefirePluginXmlDto.TestCase::getStackTrace));
        } catch (IOException e) {
            logger.error("Couldn't read the specified file!", e);
        } catch (JAXBException e) {
            logger.error("Couldn't parse the specified file!", e);
        }
        return Collections.emptyMap();
    }
}
