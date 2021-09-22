package com.mkaza.sherlock.parser.impl;

import com.google.common.base.Strings;
import com.mkaza.sherlock.parser.LogParser;
import com.mkaza.sherlock.parser.dto.SurefirePluginXmlDto;
import com.mkaza.sherlock.parser.provider.LogsProvider;
import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class XmlLogParser implements LogParser {

    private static final Logger logger = Logger.getLogger(XmlLogParser.class);

    private static final String PROLOG_CONTENT_EX_MESSAGE = "Content is not allowed in prolog.";

    @Override
    public Map<String, String> parse(LogsProvider logsProvider) {
        try {

            JAXBContext context = JAXBContext.newInstance(SurefirePluginXmlDto.class);

            Unmarshaller unmarshaller = context.createUnmarshaller();

            List<SurefirePluginXmlDto> xmlLogs = new ArrayList<>();

            for (Supplier<InputStream> logSupplier: logsProvider.getLogs()){
                unmarshal(unmarshaller, logSupplier).ifPresent(xmlLogs::add);
            }

            return xmlLogs.stream()
                    .flatMap(x -> x.getTestCases().stream())
                    .filter(tc -> !Strings.isNullOrEmpty(tc.getStackTrace()))
                    .collect(Collectors.toMap(
                            tc -> tc.getClassname() + "." + tc.getName(),
                            SurefirePluginXmlDto.TestCase::getStackTrace));

        } catch (JAXBException e) {
            logger.error("Couldn't create unmarshaller!", e);
        }
        return Collections.emptyMap();
    }

    private Optional<SurefirePluginXmlDto> unmarshal(Unmarshaller unmarshaller, Supplier<InputStream> logSupplier) {
        try {
            return Optional.of((SurefirePluginXmlDto) unmarshaller.unmarshal(logSupplier.get()));
        } catch (JAXBException e) {
            if (e.getLinkedException() instanceof SAXParseException
                    && PROLOG_CONTENT_EX_MESSAGE.equals(e.getLinkedException().getMessage())) {
                logger.warn("The log is most likely not xml!");
            } else {
                logger.error("Couldn't parse the logs!", e);
            }
        }
        return Optional.empty();
    }
}
