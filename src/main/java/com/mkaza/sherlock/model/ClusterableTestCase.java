package com.mkaza.sherlock.model;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.spark.ml.linalg.SparseVector;
import org.apache.spark.sql.Row;

import java.util.Objects;

public class ClusterableTestCase implements TestCase, Clusterable {

    private final Row row;

    public ClusterableTestCase(Row row) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClusterableTestCase that = (ClusterableTestCase) o;
        return Objects.equals(row, that.row);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row);
    }
}
