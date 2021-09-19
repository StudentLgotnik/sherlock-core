package com.mkaza.sherlock.api;

import com.mkaza.sherlock.model.TestCaseCluster;
import com.mkaza.sherlock.parser.MockParser;
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
        var relPath = Paths.get("src", "test", "resources", "SampleTestData.txt");
        testLogsPath = relPath.toFile().getAbsolutePath();
    }

    @Test
    @Ignore
    public void PositiveTestCaseClustering() {
        //setup
        SherlockConfig config = SherlockConfig.builder(testLogsPath).parser(new MockParser()).build();

        Sherlock<TestCaseCluster> sherlock = new TestCaseSherlock(config);

        //execute
        List<TestCaseCluster> clusters = sherlock.cluster();

        //assert
        Assert.assertEquals(2, clusters.size());
        Assert.assertEquals(6, clusters.get(0).getCases().size());
        Assert.assertEquals(5, clusters.get(1).getCases().size());
    }

    @Test
    @Ignore
    public void concurrencyTest() throws InterruptedException, ExecutionException {
        ExecutorService executor
                = Executors.newFixedThreadPool(2);

        SherlockConfig config = SherlockConfig.builder(testLogsPath).parser(new MockParser()).build();

        Sherlock<TestCaseCluster> sherlock = new TestCaseSherlock(config);

        SherlockConfig config2 = SherlockConfig.builder("D:\\Labs\\Masters_diploma\\project\\SherlockLogsProvider\\target\\surefire-reports\\TEST-com.mkaza.sherlock.api.UserApiTest.xml").build();

        Sherlock<TestCaseCluster> sherlock2 = new TestCaseSherlock(config2);

        Future<List<TestCaseCluster>> future1 = executor.submit(sherlock::cluster);

        Future<List<TestCaseCluster>> future2 = executor.submit(sherlock2::cluster);

        while(!future1.isDone() && !future2.isDone()) {
            System.out.println("Calculating...");
            Thread.sleep(300);
        }

        List<TestCaseCluster> result1 = future1.get();
        List<TestCaseCluster> result12 = future2.get();
    }

    public Future<Integer> calculate(Integer input) {
        ExecutorService executor
                = Executors.newSingleThreadExecutor();
        return executor.submit(() -> {
            Thread.sleep(1000);
            return input * input;
        });
    }
}
