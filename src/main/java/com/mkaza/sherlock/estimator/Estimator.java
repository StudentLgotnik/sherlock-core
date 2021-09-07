package com.mkaza.sherlock.estimator;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;

public interface Estimator {

    Dataset<Row> estimate(List<Row> rows);
}
