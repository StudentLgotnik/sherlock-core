package com.mkaza.sherlock;

import org.apache.commons.math3.ml.clustering.Cluster;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ContentVectorizerExample {

    public static void main(String[] args) {
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

        List<ClusterableRow> rescaledDataRows = rescaledData.collectAsList().stream().map(ClusterableRow::new).collect(Collectors.toList());

        List<double[]> rescaledDataRowsPoints = rescaledDataRows.stream().map(ClusterableRow::getPoint).collect(Collectors.toList());

        final DBSCANClusterer<ClusterableRow> transformer =
                new DBSCANClusterer<>(1.1, 1);
        final List<Cluster<ClusterableRow>> clusters = transformer.cluster(rescaledDataRows);
    }
}
