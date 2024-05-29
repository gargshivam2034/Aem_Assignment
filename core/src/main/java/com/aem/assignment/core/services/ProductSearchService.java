package com.aem.assignment.core.services;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Service class to search for products in the repository.
 */
@Component(service = ProductSearchService.class)
public class ProductSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSearchService.class);
    private static final String PRODUCT_PATH = "/var/commerce/products/we-retail";
    private static final String QUERY_TYPE = "nt:unstructured";
    private static final String QUERY_LIMIT = "10";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    /**
     * Searches for products matching the given query.
     *
     * @param query the search query.
     * @param resourceResolver the resource resolver to use for the search.
     * @return a list of product names that match the search query.
     */
    public List<String> searchProducts(String query, ResourceResolver resourceResolver) {
        List<String> result = new ArrayList<>();
        try {
            Map<String, String> queryMap = buildQueryMap(query);
            Iterator<Resource> resources = executeQuery(resourceResolver, queryMap);
            result = extractProductNames(resources);
        } catch (Exception e) {
            LOGGER.error("Error occurred while searching for products: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * Builds the query map for searching products.
     *
     * @param query the search query.
     * @return a map containing the query parameters.
     */
    private Map<String, String> buildQueryMap(String query) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", PRODUCT_PATH);
        queryMap.put("type", QUERY_TYPE);
        queryMap.put("fulltext", query);
        queryMap.put("p.limit", QUERY_LIMIT);
        return queryMap;
    }

    /**
     * Executes the query to find resources.
     *
     * @param resourceResolver the resource resolver to use for the search.
     * @param queryMap the map containing query parameters.
     * @return an iterator over the found resources.
     */
    private Iterator<Resource> executeQuery(ResourceResolver resourceResolver, Map<String, String> queryMap) {
        return resourceResolver.findResources(queryMap.toString(), "sql2");
    }

    /**
     * Extracts product names from the found resources.
     *
     * @param resources an iterator over the found resources.
     * @return a list of product names.
     */
    private List<String> extractProductNames(Iterator<Resource> resources) {
        List<String> productNames = new ArrayList<>();
        while (resources.hasNext()) {
            Resource resource = resources.next();
            ValueMap properties = resource.getValueMap();
            String productName = properties.get("jcr:title", String.class);
            if (productName != null) {
                productNames.add(productName);
            }
        }
        return productNames;
    }
}
