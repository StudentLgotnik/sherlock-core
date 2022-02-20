package com.mkaza.sherlock.api;

import com.mkaza.sherlock.clusterer.SherlockClusterer;
import com.mkaza.sherlock.clusterer.SherlockClustererFactory;
import com.mkaza.sherlock.estimator.Estimator;
import com.mkaza.sherlock.estimator.TfidfEstimator;
import com.mkaza.sherlock.model.ClusterableTestCase;
import com.mkaza.sherlock.model.RowStruct;
import com.mkaza.sherlock.model.TestCaseCluster;
import com.mkaza.sherlock.parser.LogParser;
import com.mkaza.sherlock.parser.LogParserFactory;
import com.mkaza.sherlock.parser.LogParserType;
import com.mkaza.sherlock.parser.provider.LogsProvider;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TestCaseSherlock implements Sherlock<TestCaseCluster> {

    private static final Logger logger = Logger.getLogger(TestCaseSherlock.class);

    private final SherlockConfig sherlockConfig;

    public TestCaseSherlock(SherlockConfig sherlockConfig) {
        this.sherlockConfig = sherlockConfig;
    }

    @Override
    public List<TestCaseCluster> cluster() {

        LogParser parser = Objects.nonNull(sherlockConfig.getParser())
                ? sherlockConfig.getParser()
                : LogParserFactory.getParser(LogParserType.DEFAULT);

        LogsProvider logsProvider = sherlockConfig.getLogsProvider();

        Map<String, String> logs = parser.parse(logsProvider);

        logger.info(String.format("%d test cases were parsed.", logs.size()));

        List<Row> rows = logs.entrySet().stream()
                .map(e -> RowFactory.create(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        StructType schema = new StructType(new StructField[]{
                new StructField(RowStruct.TEST_NAME.field(), DataTypes.StringType, false, Metadata.empty()),
                new StructField(RowStruct.TEST_ERRORS.field(), DataTypes.StringType, false, Metadata.empty())
        });

        Estimator estimator = TfidfEstimator.createForSchema(schema);

        Dataset<Row> dataset = estimator.estimate(rows);

        List<Row> estimatedRows = dataset.collectAsList();

        logger.info(String.format("%d test cases were estimated.", estimatedRows.size()));

        //Cluster dataset
        SherlockClusterer<ClusterableTestCase> clusterer = SherlockClustererFactory
                .createClusterer(sherlockConfig.getClustererConfig());

        List<ClusterableTestCase> clusterableDataSet = estimatedRows.stream().map(ClusterableTestCase::new).collect(Collectors.toList());

        List<Cluster<ClusterableTestCase>> clusters = clusterer.cluster(clusterableDataSet);

        logger.info(String.format("%d clusters were found.", clusters.size()));

        return clusters.stream().map(TestCaseCluster::new).collect(Collectors.toList());
    }
}
