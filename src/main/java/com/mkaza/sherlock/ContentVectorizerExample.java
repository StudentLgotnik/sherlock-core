package com.mkaza.sherlock;

import com.mkaza.sherlock.model.ClusterableDataPoint;
import com.mkaza.sherlock.util.DbscanUtil;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContentVectorizerExample {

    public static void main(String[] args) throws URISyntaxException, IOException {

//        Dataset<Row> rescaledData = getTfIdfDataSet();
//
//        List<ClusterableRow> rescaledDataRows = rescaledData.collectAsList().stream().map(ClusterableRow::new).collect(Collectors.toList());
//
//        List<double[]> rescaledDataRowsPoints = rescaledDataRows.stream().map(ClusterableRow::getPoint).collect(Collectors.toList());

        List<Clusterable> rescaledDataRows = parseResourcePoints();

        double epsilon = DbscanUtil.calcAverageEpsilon(rescaledDataRows, 3);

        int minPts = DbscanUtil.calcMinPts(rescaledDataRows.size());

        final DBSCANClusterer<Clusterable> transformer =
                new DBSCANClusterer<>(epsilon, minPts);
        final List<Cluster<Clusterable>> clusters = transformer.cluster(rescaledDataRows);
    }

    private static Dataset<Row> getTfIdfDataSet() {
        // create a spark session
        SparkSession spark = SparkSession
                .builder()
                .appName("TFIDF Example")
                .master("local[2]")
                .getOrCreate();

        // documents corpus. each row is a document.
        List<Row> data = Arrays.asList(
                RowFactory.create("He love cat"),//
                RowFactory.create("He love people"),//
                RowFactory.create("We love animals"),
                RowFactory.create("We hate people"),//
                RowFactory.create("She hate cat"),
                RowFactory.create("People hate People")//
        );
        StructType schema = new StructType(new StructField[]{
                new StructField("sentence", DataTypes.StringType, false, Metadata.empty())
        });

        // import data with the schema
        Dataset<Row> sentenceData = spark.createDataFrame(data, schema);

        // break sentence to words
        Tokenizer tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words");
        Dataset<Row> wordsData = tokenizer.transform(sentenceData);

        CountVectorizer countVectorizer = new CountVectorizer().setInputCol("words").setOutputCol("rawFeatures");

        CountVectorizerModel  cvm = countVectorizer.fit(wordsData);

        Dataset<Row> cvmData = cvm.transform(wordsData);

        IDF idf = new IDF().setInputCol("rawFeatures").setOutputCol("features");
        IDFModel idfModel = idf.fit(cvmData);

        Dataset<Row> rescaledData =  idfModel.transform(cvmData);

        return rescaledData;
    }

    private static List<Clusterable> parseResourcePoints() throws URISyntaxException, IOException {
        URL xAxis = ContentVectorizerExample.class.getResource("/xAxis.txt");
        URL yAxis = ContentVectorizerExample.class.getResource("/yAxis.txt");

        String xPointData = Files.readString(Paths.get(xAxis.toURI())).replaceAll("([\\r\\n])", "");
        String yPointData = Files.readString(Paths.get(yAxis.toURI())).replaceAll("([\\r\\n])", "");

        double[] xAxisArray = Arrays.stream(xPointData.split(" "))
                .filter(item-> !item.isEmpty())
                .mapToDouble(Double::valueOf)
                .toArray();
        double[] yAxisArray = Arrays.stream(yPointData.split(" "))
                .filter(item-> !item.isEmpty())
                .mapToDouble(Double::valueOf)
                .toArray();

        List<Clusterable> dataPoints = new ArrayList<>();

        for (int i = 0; i < xAxisArray.length; i++) {
            dataPoints.add(new ClusterableDataPoint(new double[]{xAxisArray[i], yAxisArray[i]}));
        }

        return dataPoints;
    }
}
