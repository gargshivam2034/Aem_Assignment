package com.aem.assignment.core.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface ReplicatingAgentService {
    public String ApiFetchData();
    public void CreatePage(ResourceResolver resourceResolver);
}
