package com.mkaza.sherlock;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.IDF;
import org.apache.spark.ml.feature.IDFModel;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.ml.linalg.SparseVector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class TFIDFExample {

    public static void main(String[] args){



        // create a spark session
        SparkSession spark = SparkSession
                .builder()
                .appName("TFIDF Example")
                .master("local[2]")
//                .enableHiveSupport()
                .getOrCreate();

        // documents corpus. each row is a document.
        List<Row> data = Arrays.asList(
                RowFactory.create("He love cat"),
                RowFactory.create("He love people"),
                RowFactory.create("We love animals"),
                RowFactory.create("We hate people"),
                RowFactory.create("She hate cat"),
                RowFactory.create("People hate animals")
        );
        StructType schema = new StructType(new StructField[]{
//                new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
                new StructField("sentence", DataTypes.StringType, false, Metadata.empty())
        });

        // import data with the schema
        Dataset<Row> sentenceData = spark.createDataFrame(data, schema);

        // break sentence to words
        Tokenizer tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words");
        Dataset<Row> wordsData = tokenizer.transform(sentenceData);

        // define Transformer, HashingTF
        int numFeatures = 8; // He love cat people we animals hate she
        HashingTF hashingTF = new HashingTF()
                .setInputCol("words")
                .setOutputCol("rawFeatures");
//                .setNumFeatures(numFeatures);

        // transform words to feature vector
        Dataset<Row> featurizedData = hashingTF.transform(wordsData);

        System.out.println("TF vectorized data\n----------------------------------------");
        for(Row row:featurizedData.collectAsList()){
            System.out.println(row.get(2));
        }

        System.out.println(featurizedData.toJSON());

        // IDF is an Estimator which is fit on a dataset and produces an IDFModel
        IDF idf = new IDF().setInputCol("rawFeatures").setOutputCol("features");
        IDFModel idfModel = idf.fit(featurizedData);

        // The IDFModel takes feature vectors (generally created from HashingTF or CountVectorizer) and scales each column
        Dataset<Row> rescaledData = idfModel.transform(featurizedData);

        List<ClusterableRow> rescaledDataRows = rescaledData.collectAsList().stream().map(ClusterableRow::new).collect(Collectors.toList());

        List<double[]> rescaledDataRowsPoints = rescaledDataRows.stream().map(ClusterableRow::getPoint).collect(Collectors.toList());

        final DBSCANClusterer<ClusterableRow> transformer =
                new DBSCANClusterer<>(1.0, 1);
        final List<Cluster<ClusterableRow>> clusters = transformer.cluster(rescaledDataRows);

        System.out.println("TF-IDF vectorized data\n----------------------------------------");
        for(Row row:rescaledData.collectAsList()){
            System.out.println(row.get(3));
        }

        System.out.println("Transformations\n----------------------------------------");
        for(Row row:rescaledData.collectAsList()){
            System.out.println(row);
        }

        spark.close();

    }
}
