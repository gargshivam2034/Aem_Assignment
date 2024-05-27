package com.aem.assignment.core.services;


import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.json.JSONObject;

public interface QuerySearchService {

    JSONObject getSearchResult(String searchText, ResourceResolver resourceResolver) throws LoginException, NoSuchMethodException;

}
