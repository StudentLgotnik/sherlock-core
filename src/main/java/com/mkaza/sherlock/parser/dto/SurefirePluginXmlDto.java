package com.mkaza.sherlock.parser.dto;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = "testsuite")
@XmlAccessorType(XmlAccessType.FIELD)
public class SurefirePluginXmlDto {

    @XmlElement(name = "property")
    @XmlElementWrapper(name = "properties")
    private List<Property> properties = new ArrayList<>();

    @XmlElement(name = "testcase")
    private List<TestCase> testCases = new ArrayList<>();

    @Data
    @XmlRootElement(name = "property")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Property {

        @XmlAttribute
        private String name;
        @XmlAttribute
        private String value;

    }

    @Data
    @XmlRootElement(name = "testcase")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TestCase {

        @XmlAttribute
        private String name;
        @XmlAttribute
        private String classname;
        @XmlAttribute
        private double time;
        @XmlElement(name = "failure")
        private TestCaseFailure failure;
        @XmlElement(name = "error")
        private TestCaseError error;

        public String getStackTrace() {
            return failure != null ? failure.getValue() : Strings.EMPTY +
                    (error != null ? error.getValue() : Strings.EMPTY);
        }

    }

    @Data
    @XmlRootElement(name = "testcase")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TestCaseFailure {

        @XmlAttribute
        private String type;
        @XmlValue
        private String value;

    }

    @Data
    @XmlRootElement(name = "error")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TestCaseError {

        @XmlAttribute
        private String type;
        @XmlAttribute
        private String message;
        @XmlValue
        private String value;

    }

}
