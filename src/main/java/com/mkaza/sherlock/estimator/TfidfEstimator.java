package com.mkaza.sherlock.estimator;

import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

import java.util.List;

public class TfidfEstimator {

    public Dataset<Row> estimate(List<Row> rows, StructType schema) {
        // create a spark session
        SparkSession spark = SparkSession
                .builder()
                .appName("TFIDF Example")
                .master("local[2]")
                .getOrCreate();

        // import data with the schema
        Dataset<Row> sentenceData = spark.createDataFrame(rows, schema);

        // break sentence to words
        Tokenizer tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words");
        Dataset<Row> wordsData = tokenizer.transform(sentenceData);

        CountVectorizer countVectorizer = new CountVectorizer().setInputCol("words").setOutputCol("rawFeatures");

        CountVectorizerModel cvm = countVectorizer.fit(wordsData);

        Dataset<Row> cvmData = cvm.transform(wordsData);

        IDF idf = new IDF().setInputCol("rawFeatures").setOutputCol("features");
        IDFModel idfModel = idf.fit(cvmData);

        spark.close();

        return idfModel.transform(cvmData);
    }
}
