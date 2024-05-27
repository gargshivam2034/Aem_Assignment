package com.aem.assignment.core.bean;

import org.apache.commons.lang3.StringUtils;

/**
 *
 *
 */
public class ClientResponse {
    private int statusCode = 0;
    private String data = StringUtils.EMPTY;


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }
}
