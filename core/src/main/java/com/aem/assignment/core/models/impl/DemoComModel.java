package com.aem.assignment.core.models.impl;


import org.apache.sling.api.SlingHttpServletRequest;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.junit.rules.OSGiService;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DemoComModel {

    @ValueMapValue
    private String title;
    @ValueMapValue
    private  String path;

    public String getPath()  { return path;}
    public String getTitle() {
        return title.toUpperCase();
    }

}
