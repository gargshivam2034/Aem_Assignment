package com.aem.assignment.core.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface ReadExcelDataService {

    /**
     * Retrieves data from an Excel file located at the specified path using the provided resource resolver.
     *
     * @param resolver The resource resolver used to access the Excel file.
     * @param path     The path to the Excel file.
     */
    void getDataFromExcel(final ResourceResolver resolver, String path);


}
