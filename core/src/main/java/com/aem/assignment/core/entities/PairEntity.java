package com.aem.assignment.core.entities;

public class PairEntity {
    String key;
    String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public PairEntity(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
