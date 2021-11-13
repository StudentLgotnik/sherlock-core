package com.mkaza.sherlock.model;

/**
 * Model to represent test case during the clusterization.
 */
public interface TestCase {

    /**
     * Get name of test case
     * @param <T> name type
     * @return test name
     */
    <T> T getTestName();

    /**
     * Get stack trace of test case
     * @param <T> stack trace type
     * @return stack trace
     */
    <T> T getTestErrors();
}
