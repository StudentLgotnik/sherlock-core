package com.mkaza.sherlock.api;

import com.mkaza.sherlock.clusterer.SherlockClusterer;
import com.mkaza.sherlock.estimator.TfidfEstimator;
import com.mkaza.sherlock.model.ClusterableTestCase;
import com.mkaza.sherlock.model.RowStruct;
import com.mkaza.sherlock.model.TestCaseCluster;
import com.mkaza.sherlock.parser.LogParser;
import com.mkaza.sherlock.parser.LogParserFactory;
import com.mkaza.sherlock.parser.LogParserType;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                : LogParserFactory.getParser(LogParserType.XML);

        String pathToLogs = sherlockConfig.getPathToLogs();

        if (!Files.exists(Paths.get(pathToLogs))) {
            throw new IllegalArgumentException(String.format("Path to logs is invalid: %s", pathToLogs));
        }

        Map<String, String> logs = Files.isRegularFile(Paths.get(pathToLogs))
                ? parser.parse(pathToLogs)
                : parseDir(pathToLogs, parser);

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

        List<Cluster<ClusterableTestCase>> clusters = Objects.nonNull(sherlockConfig.getClustererConfig())
                ? clusterer.cluster(clusterableDataSet, sherlockConfig.getClustererConfig())
                : clusterer.cluster(clusterableDataSet);

        return clusters.stream().map(TestCaseCluster::new).collect(Collectors.toList());
    }

    public Map<String, String> parseDir(String logDirPath, LogParser parser) {
        try (Stream<Path> paths = Files.walk(Paths.get(logDirPath))) {
            return paths
                    .filter(Files::isRegularFile)
                    .flatMap(p -> parser.parse(p.toAbsolutePath().toString()).entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (IOException e) {
            logger.error(String.format("Couldn't read from the directory %s!", logDirPath), e);
        }
        return Collections.emptyMap();
    }
}
