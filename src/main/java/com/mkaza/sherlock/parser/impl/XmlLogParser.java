package com.mkaza.sherlock.parser.impl;

import com.google.common.base.Strings;
import com.mkaza.sherlock.parser.LogParser;
import com.mkaza.sherlock.parser.dto.SurefirePluginXmlDto;
import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XmlLogParser implements LogParser {

    private static final Logger logger = Logger.getLogger(XmlLogParser.class);

    private static final String PROLOG_CONTENT_EX_MESSAGE = "Content is not allowed in prolog.";

    @Override
    public Map<String, String> parse(String logFilePath) {

        try(FileReader reader = new FileReader(logFilePath)) {

            JAXBContext context = JAXBContext.newInstance(SurefirePluginXmlDto.class);

            SurefirePluginXmlDto surefirePluginXmlDto = (SurefirePluginXmlDto) context
                    .createUnmarshaller()
                    .unmarshal(reader);

            logger.info(String.format("Successfully parsed logfile %s!", Paths.get(logFilePath).getFileName()));

            return surefirePluginXmlDto.getTestCases().stream()
                    .filter(tc -> !Strings.isNullOrEmpty(tc.getStackTrace()))
                    .collect(Collectors.toMap(
                            tc -> tc.getClassname() + "." + tc.getName(),
                            SurefirePluginXmlDto.TestCase::getStackTrace));
        } catch (IOException e) {
            logger.error(String.format("Couldn't read the file %s!", logFilePath), e);
        } catch (JAXBException e) {
            if (e.getLinkedException() instanceof SAXParseException
                    && PROLOG_CONTENT_EX_MESSAGE.equals(e.getLinkedException().getMessage())) {
                logger.warn(String.format("The file is most likely not xml %s!", logFilePath));
            } else {
                logger.error(String.format("Couldn't parse the file %s!", logFilePath), e);
            }
        }
        return Collections.emptyMap();
    }
}
