package com.mkaza.sherlock.estimator;

import com.mkaza.sherlock.model.RowStruct;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

import java.util.List;

public class TfidfEstimator implements Estimator {

    private static final String MASTER = "local[2]";
    private final StructType schema;

    private TfidfEstimator(StructType schema) {
        this.schema = schema;
    }

    @Override
    public Dataset<Row> estimate(List<Row> rows) {
        // create a spark session
        SparkSession spark = SparkSession
                .builder()
                .master(MASTER)
                .getOrCreate();

        // import data with the schema
        Dataset<Row> sentenceData = spark.createDataFrame(rows, schema);

        // break sentence to words
        Tokenizer tokenizer = new Tokenizer().setInputCol(RowStruct.TEST_ERRORS.field()).setOutputCol(RowStruct.WORDS_DICTIONARY.field());
        Dataset<Row> wordsData = tokenizer.transform(sentenceData);

        CountVectorizer countVectorizer = new CountVectorizer().setInputCol(RowStruct.WORDS_DICTIONARY.field()).setOutputCol(RowStruct.RAW_FEATURES.field());

        CountVectorizerModel cvm = countVectorizer.fit(wordsData);

        Dataset<Row> cvmData = cvm.transform(wordsData);

        IDF idf = new IDF().setInputCol(RowStruct.RAW_FEATURES.field()).setOutputCol(RowStruct.FEATURES.field());
        IDFModel idfModel = idf.fit(cvmData);

        spark.close();

        return idfModel.transform(cvmData);
    }

    public static TfidfEstimator createForSchema(StructType schema) {
        return new TfidfEstimator(schema);
    }
}
