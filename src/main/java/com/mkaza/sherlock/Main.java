package com.mkaza.sherlock;

import com.mkaza.sherlock.clusterer.SherlockClusterer;
import com.mkaza.sherlock.estimator.TfidfEstimator;
import com.mkaza.sherlock.model.ClusterableRow;
import com.mkaza.sherlock.parser.LogParser;
import com.mkaza.sherlock.parser.MockParser;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {

    private static final String LINE_SPLITTER = "\n-------------------------------------";

    public static void main(String[] args) throws URISyntaxException {

        //Get logs as separate text units
        LogParser parser = new MockParser();
        List<String> logs = parser.parseFile(Paths.get(Objects.requireNonNull(ContentVectorizerExample.class.getResource("/tfidf.txt")).toURI()).toString());
        System.out.println("Collected logs: \n" + String.join("\n", logs));

        //Generate dataset from rows
        List<Row> rows = logs.stream().map(RowFactory::create).collect(Collectors.toList());

        StructType schema = new StructType(new StructField[]{
                new StructField("sentence", DataTypes.StringType, false, Metadata.empty())
        });

        TfidfEstimator tfidfEstimator = new TfidfEstimator();

        Dataset<Row> dataset = tfidfEstimator.estimate(rows, schema);

        //Cluster dataset
        SherlockClusterer clusterer = new SherlockClusterer();

        List<ClusterableRow> clusterableDataSet = dataset.collectAsList().stream().map(ClusterableRow::new).collect(Collectors.toList());

        List<Cluster<ClusterableRow>> clusters = clusterer.cluster(clusterableDataSet);
        clusters.forEach(
                c -> System.out.println(
                                "Cluster: " + clusters.indexOf(c) +
                                " Data: \n" + c.getPoints().stream().map(ClusterableRow::getLogText).collect(Collectors.joining(",\n")) +
                                LINE_SPLITTER)
        );
    }
}
