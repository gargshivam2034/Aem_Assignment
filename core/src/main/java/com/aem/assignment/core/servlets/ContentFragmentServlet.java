package com.aem.assignment.core.servlets;


import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.ServletException;
import java.io.IOException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.framework.Constants;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class,property =
        {
                ServletResolverConstants.SLING_SERVLET_METHODS+"="+ HttpConstants.METHOD_GET,
                "sling.servlet.paths=/bin/fetchContentFragment"
        }
)
public class ContentFragmentServlet extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            ResourceResolver resourceResolver = request.getResourceResolver();
            String contentFragmentPath = "/content/dam/we-retail/en/faqs/my-custom-contnent-fragment";
            Resource contentFragmentResource = resourceResolver.getResource(contentFragmentPath);
            assert contentFragmentResource != null;
            Resource resource=contentFragmentResource.getChild("jcr:content");
            if (resource!= null) {
                ValueMap properties = resource.getValueMap();
                JSONObject json = new JSONObject(properties);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json.toString());
            } else {
                response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
        }
    }
}
