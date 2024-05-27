package com.aem.assignment.core.models.impl;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.List;


@Model(adaptables = SlingHttpServletRequest.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class OSGIFactoryConfigModel {

//    @OSGiService
//    private OSGIFactoryConfigImpl osgiFactoryConfigImpl;

//    public List<OSGIFactoryConfig> getAllOSGIConfig()
//    {
//        return osgiFactoryConfigImpl.getConfigList();
// }

    public  List<RequestParameter> getQueryParameter()
    {
        List<RequestParameter> list = request.getRequestParameterList();
        return list;
    }

    @SlingObject
    private SlingHttpServletRequest request;

    @PostConstruct
    protected void init(){
        request.getRequestURL();
    }
}
