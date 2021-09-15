package com.mkaza.sherlock.model;

public interface TestCase {

    <T> T getTestName();

    <T> T getTestErrors();
}
