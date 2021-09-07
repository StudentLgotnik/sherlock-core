package com.mkaza.sherlock.model;

public enum RowStruct {
    TEST_NAME("test_name"),
    TEST_ERRORS("test_errors"),
    WORDS_DICTIONARY("words_dictionary"),
    RAW_FEATURES("raw_features"),
    FEATURES("features");

    private final String field;

    RowStruct(String field) {
        this.field = field;
    }

    public String field() {
        return field;
    }
}
