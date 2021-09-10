package com.mkaza.sherlock.api;

import com.mkaza.sherlock.clusterer.SherlockClusterer;
import com.mkaza.sherlock.estimator.TfidfEstimator;
import com.mkaza.sherlock.model.*;
import com.mkaza.sherlock.parser.LogParser;
import com.mkaza.sherlock.parser.LogParserFactory;
import com.mkaza.sherlock.parser.LogParserType;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestCaseSherlock implements Sherlock<TestCaseCluster> {

    private final SherlockConfig sherlockConfig;

    public TestCaseSherlock(SherlockConfig sherlockConfig) {
        this.sherlockConfig = sherlockConfig;
    }

    @Override
    public List<TestCaseCluster> cluster() {

        LogParser parser = sherlockConfig.getParser() != null
                ? sherlockConfig.getParser()
                : LogParserFactory.getParser(LogParserType.XML);

        Map<String, String> logs = parser.parse(sherlockConfig.getLogFilePath());

        List<Row> rows = logs.entrySet().stream()
                .map(e -> RowFactory.create(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        StructType schema = new StructType(new StructField[]{
                new StructField(RowStruct.TEST_NAME.field(), DataTypes.StringType, false, Metadata.empty()),
                new StructField(RowStruct.TEST_ERRORS.field(), DataTypes.StringType, false, Metadata.empty())
        });

        TfidfEstimator tfidfEstimator = TfidfEstimator.createForSchema(schema);

        Dataset<Row> dataset = tfidfEstimator.estimate(rows);

        //Cluster dataset
        SherlockClusterer<ClusterableTestCase> clusterer = new SherlockClusterer<>();

        List<ClusterableTestCase> clusterableDataSet = dataset.collectAsList().stream().map(ClusterableTestCase::new).collect(Collectors.toList());

        List<Cluster<ClusterableTestCase>> clusters = clusterer.cluster(clusterableDataSet);

        return clusters.stream().map(TestCaseCluster::new).collect(Collectors.toList());
    }
}
