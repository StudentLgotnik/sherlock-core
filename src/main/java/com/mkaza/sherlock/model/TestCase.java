package com.mkaza.sherlock.model;

import org.apache.commons.math3.ml.clustering.Clusterable;

public interface TestCase {

    <T> T getTestName();

    <T> T getTestErrors();
}
