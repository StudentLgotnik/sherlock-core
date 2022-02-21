package com.mkaza.sherlock.cli;

import com.mkaza.sherlock.api.Sherlock;
import com.mkaza.sherlock.api.SherlockConfig;
import com.mkaza.sherlock.api.TestCaseSherlock;
import com.mkaza.sherlock.clusterer.impl.DBSCANAlgorithm;
import com.mkaza.sherlock.clusterer.impl.DBSCANParameters;
import com.mkaza.sherlock.model.TestCaseCluster;
import com.mkaza.sherlock.parser.impl.XmlLogParser;
import com.mkaza.sherlock.parser.provider.impl.DirLogsProvider;

import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        var relPath = Paths.get("src", "test", "resources", "surefire", "reports");
        var testLogsPath = relPath.toFile().getAbsolutePath();

        //Setup sherlock configuration via builder
        SherlockConfig config = SherlockConfig
                .builder(
                        //required logs provider to know how to receive your data or use default one(FileLogsProvider or DirLogsProvider)
                        new DirLogsProvider(testLogsPath),
                        //required clustering algorithm to specify which algorithm use for clustering
                        new DBSCANAlgorithm(
                                //initial algorithm parameters, not always required
                                DBSCANParameters.builder()
                                        //search radius for neighboring points during clustering
                                        .epsilon(5.642)
                                        // the number of points required in a radius(epsilon) to assign a point to a cluster
                                        .minPts(2)
                                        .build()
                        ))
                //parser to determine how to parse your logs
                .parser(new XmlLogParser())
                .build();

        //Initialize sherlock clusterer based on configuration
        Sherlock<TestCaseCluster> sherlock = new TestCaseSherlock(config);

        //Execute sherlock clusterer to receive clusters
        List<TestCaseCluster> clusters = sherlock.cluster();
    }
}
