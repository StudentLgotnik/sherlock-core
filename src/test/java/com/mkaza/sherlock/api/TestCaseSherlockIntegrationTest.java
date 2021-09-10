package com.mkaza.sherlock.api;

import com.mkaza.sherlock.model.TestCaseCluster;
import com.mkaza.sherlock.parser.LogParserFactory;
import com.mkaza.sherlock.parser.LogParserType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;


public class TestCaseSherlockIntegrationTest {

    private static String testLogsPath;

    @BeforeClass
    public static void setUp() throws URISyntaxException {
        var relPath = Paths.get("src", "test", "resources", "SampleTestData.txt");
        testLogsPath = relPath.toFile().getAbsolutePath();
    }

    @Test
    public void PositiveTestCaseClustering() {
        //setup
        SherlockConfig config = SherlockConfig.builder(testLogsPath).parser(LogParserFactory.getParser(LogParserType.MOCK)).build();

        Sherlock<TestCaseCluster> sherlock = new TestCaseSherlock(config);

        //execute
        List<TestCaseCluster> clusters = sherlock.cluster();

        //assert
        Assert.assertEquals(2, clusters.size());
        Assert.assertEquals(6, clusters.get(0).getCases().size());
        Assert.assertEquals(5, clusters.get(1).getCases().size());
    }
}
