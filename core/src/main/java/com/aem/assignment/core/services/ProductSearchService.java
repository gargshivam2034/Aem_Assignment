package com.aem.assignment.core.services;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


import java.util.*;

@Component(service = ProductSearchService.class)
public class ProductSearchService {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    public List<String> searchProducts(String query,ResourceResolver resourceResolver) {
        List<String> result = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        try{
            String queryPath = "/var/commerce/products/we-retail";
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put("path", queryPath);
            queryMap.put("type", "nt:unstructured");
            queryMap.put("fulltext", query);
            queryMap.put("p.limit", "10");
            Iterator<Resource> resources = resourceResolver.findResources(queryMap.toString(),"sql2");
            while (resources.hasNext()) {
                Resource resource = resources.next();
                ValueMap properties = resource.getValueMap();
                String productName = properties.get("jcr:title", String.class);
                if (productName != null) {
                    result.add(productName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
