package com.mkaza.sherlock.model;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.spark.ml.linalg.SparseVector;
import org.apache.spark.sql.Row;

import java.util.Arrays;

public class ClusterableRow implements Clusterable {

    private final Row row;

    public ClusterableRow(Row row) {
        this.row = row;
    }

    @Override
    public double[] getPoint() {
        SparseVector vector = row.getAs(RowStruct.FEATURES.field());

        double[] points =  new double[vector.size()];
        for (int i = 0; i < vector.values().length; i++) {
            points[vector.indices()[i]] = vector.values()[i];
        }

        return points;
    }

    public <T> T getTestName() {
        return row.getAs(RowStruct.TEST_NAME.field());
    }

    public <T> T getTestErrors() {
        return row.getAs(RowStruct.TEST_ERRORS.field());
    }

    public <T> T getWordsDictionary() {
        return row.getAs(RowStruct.WORDS_DICTIONARY.field());
    }

    public <T> T getRawFeatures() {
        return row.getAs(RowStruct.RAW_FEATURES.field());
    }

    public <T> T getFeatures() {
        return row.getAs(RowStruct.FEATURES.field());
    }
}
