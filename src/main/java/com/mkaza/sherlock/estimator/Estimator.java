package com.mkaza.sherlock.estimator;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;

public interface Estimator {

    /**
     * Evaluates the rows to obtain their vector representation.
     * @param rows row with text data
     * @return estimated rows as dataset
     */
    Dataset<Row> estimate(List<Row> rows);
}
