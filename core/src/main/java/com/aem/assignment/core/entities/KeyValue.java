package com.aem.assignment.core.entities;



public class KeyValue {
    public String getKey() {
        return key;
    }

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
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

    private String key;
    private String value;
}
