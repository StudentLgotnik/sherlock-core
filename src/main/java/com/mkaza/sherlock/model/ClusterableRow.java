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
        SparseVector vector = row.getAs(3);

        double[] points =  new double[vector.size()];
        for (int i = 0; i < vector.values().length; i++) {
            points[vector.indices()[i]] = vector.values()[i];
        }

        return points;
    }

    public String getLogText() {
        return row.getAs("sentence");
    }
}
