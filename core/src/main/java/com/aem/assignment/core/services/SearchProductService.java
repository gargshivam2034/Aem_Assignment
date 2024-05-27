package com.aem.assignment.core.services;

import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

public interface SearchProductService {

    /**
     * Executes a search query to find products.
     *
     * @param query           The search query.
     * @param resourceResolver The resource resolver used for accessing the repository.
     * @return A list of strings representing the search results.
     */
    List<String> executeQuery(String query, ResourceResolver resourceResolver);

}
