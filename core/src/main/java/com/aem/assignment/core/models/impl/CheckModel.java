package com.aem.assignment.core.models.impl;

import com.aem.assignment.core.services.Check;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

@Model(adaptables = SlingHttpServletRequest.class)
public class CheckModel {


    @OSGiService(filter = "(component.name=com.aem.assignment.core.service.CheckImplA)")
    Check check;

    public String getCheck() {
         return check.getBookDetailsString();
    }
}
