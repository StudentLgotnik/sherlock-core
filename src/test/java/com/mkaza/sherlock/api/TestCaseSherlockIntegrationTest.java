package com.mkaza.sherlock.api;

import com.mkaza.sherlock.model.TestCaseCluster;
import com.mkaza.sherlock.parser.MockParser;
import com.mkaza.sherlock.parser.provider.impl.DirLogsProvider;
import com.mkaza.sherlock.parser.provider.impl.FileLogsProvider;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class TestCaseSherlockIntegrationTest {

    private static String testLogsPath;

    @BeforeClass
    public static void setUp() throws URISyntaxException {
        var relPath = Paths.get("src", "test", "resources", "surefire", "reports");
        testLogsPath = relPath.toFile().getAbsolutePath();
    }

    @Test
    @Ignore
    public void PositiveTestCaseClustering() {
        //setup
        SherlockConfig config = SherlockConfig.builder(new DirLogsProvider(testLogsPath)).build();

        Sherlock<TestCaseCluster> sherlock = new TestCaseSherlock(config);

        //execute
        List<TestCaseCluster> clusters = sherlock.cluster();

        //assert
        Assert.assertEquals(5, clusters.size());
        Assert.assertEquals(4, clusters.get(0).getCases().size());
        Assert.assertEquals(1, clusters.get(1).getCases().size());
        Assert.assertEquals(1, clusters.get(2).getCases().size());
        Assert.assertEquals(1, clusters.get(3).getCases().size());
        Assert.assertEquals(1, clusters.get(4).getCases().size());
    }

    @Test
    public void concurrencyTest() throws InterruptedException, ExecutionException {
        ExecutorService executor
                = Executors.newFixedThreadPool(2);

        var relPath = Paths.get("src", "test", "resources", "SampleTestData.txt");
        var sampleData = relPath.toFile().getAbsolutePath();

        SherlockConfig config = SherlockConfig.builder(new FileLogsProvider(sampleData)).parser(new MockParser()).build();

        Sherlock<TestCaseCluster> sherlock = new TestCaseSherlock(config);

        SherlockConfig config2 = SherlockConfig.builder(new DirLogsProvider(testLogsPath)).build();

        Sherlock<TestCaseCluster> sherlock2 = new TestCaseSherlock(config2);

        Future<List<TestCaseCluster>> future1 = executor.submit(sherlock::cluster);

        Future<List<TestCaseCluster>> future2 = executor.submit(sherlock2::cluster);

        while(!future1.isDone() && !future2.isDone()) {
            System.out.println("Calculating...");
            Thread.sleep(300);
        }

        List<TestCaseCluster> result1 = future1.get();
        List<TestCaseCluster> result2 = future2.get();
        Assert.assertFalse(CollectionUtils.isEmpty(result1));
        Assert.assertFalse(CollectionUtils.isEmpty(result2));
    }
}
