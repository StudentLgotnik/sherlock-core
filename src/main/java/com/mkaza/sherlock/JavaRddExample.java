package com.mkaza.sherlock;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.feature.IDF;
import org.apache.spark.mllib.feature.IDFModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

public class JavaRddExample {
    public static void main(String[] args) {
        String wordFile = "D:\\OwnProjects\\MasterDIploma\\src\\main\\resources\\tfidf.txt";
        SparkConf conf = new SparkConf().setAppName("TF-IDF").setMaster("local[2]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<List<String>> wordData = sc.textFile(wordFile)
                .map(new Function<String, List<String>>() {
                    /**
                     *
                     */
                    private static final long serialVersionUID = 1091920418241245797L;

                    public List<String> call(String line) throws Exception {
                        String[] words = line.split(" ");
                        return Arrays.asList(words);
                    }

                }).cache();
        HashingTF hTF = new HashingTF();
        JavaRDD<Vector> tf = hTF.transform(wordData).cache();
        IDFModel idfModel = new IDF().fit(tf);
        JavaRDD<Vector> tfidf = idfModel.transform(tf);

        SQLContext sqlContext = new SQLContext(sc);
        Dataset<Vector> sentenceData = sqlContext.createDataset(tfidf.rdd(), Encoders.bean(Vector.class));
        System.out.println(tfidf);
    }
}
