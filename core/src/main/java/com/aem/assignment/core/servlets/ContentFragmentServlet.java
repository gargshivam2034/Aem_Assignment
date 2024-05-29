package com.aem.assignment.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.framework.Constants;
import org.apache.sling.commons.json.JSONObject;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Servlet for fetching content from a specific Content Fragment.
 */
@Component(service = Servlet.class, property =
        {
                ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=/bin/fetchContentFragment"
        }
)
public class ContentFragmentServlet extends SlingAllMethodsServlet {

    /**
     * Handles the HTTP GET request to fetch content from a Content Fragment.
     *
     * @param request  The Sling HTTP servlet request object.
     * @param response The Sling HTTP servlet response object.
     * @throws ServletException If an error occurs during servlet processing.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            String contentFragmentPath = "/content/dam/we-retail/en/faqs/my-custom-contnent-fragment";
            JSONObject json = getContentFragmentJSON(request, contentFragmentPath);
            writeJSONResponse(response, json);
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    /**
     * Retrieves the properties of a Content Fragment as a JSON object.
     *
     * @param request             The Sling HTTP servlet request object.
     * @param contentFragmentPath The path to the Content Fragment.
     * @return JSONObject containing the properties of the Content Fragment.
     * @throws Exception If an error occurs during processing.
     */
    private JSONObject getContentFragmentJSON(SlingHttpServletRequest request, String contentFragmentPath) throws Exception {
        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource contentFragmentResource = getContentFragmentResource(resourceResolver, contentFragmentPath);
        if (contentFragmentResource != null) {
            Resource resource = getContentFragmentContentResource(contentFragmentResource);
            if (resource != null) {
                ValueMap properties = getContentFragmentProperties(resource);
                return createJSONObject(properties);
            }
        }
        return null;
    }

    /**
     * Retrieves the Resource representing the Content Fragment.
     *
     * @param resourceResolver     The ResourceResolver instance.
     * @param contentFragmentPath The path to the Content Fragment.
     * @return Resource representing the Content Fragment.
     */
    private Resource getContentFragmentResource(ResourceResolver resourceResolver, String contentFragmentPath) {
        return resourceResolver.getResource(contentFragmentPath);
    }

    /**
     * Retrieves the child resource representing the content of the Content Fragment.
     *
     * @param contentFragmentResource Resource representing the Content Fragment.
     * @return Child resource representing the content of the Content Fragment.
     */
    private Resource getContentFragmentContentResource(Resource contentFragmentResource) {
        return contentFragmentResource.getChild("jcr:content");
    }

    /**
     * Retrieves the properties of the Content Fragment.
     *
     * @param contentFragmentContentResource Child resource representing the content of the Content Fragment.
     * @return ValueMap containing the properties of the Content Fragment.
     */
    private ValueMap getContentFragmentProperties(Resource contentFragmentContentResource) {
        return contentFragmentContentResource.getValueMap();
    }

    /**
     * Creates a JSONObject from the provided ValueMap.
     *
     * @param properties ValueMap containing the properties of the Content Fragment.
     * @return JSONObject representing the properties of the Content Fragment.
     */
    private JSONObject createJSONObject(ValueMap properties) {
        return new JSONObject(properties);
    }

    /**
     * Writes the JSON response to the servlet response.
     *
     * @param response The Sling HTTP servlet response object.
     * @param json     The JSONObject to be written as the response.
     * @throws IOException If an I/O error occurs.
     */
    private void writeJSONResponse(SlingHttpServletResponse response, JSONObject json) throws IOException {
        if (json != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json.toString());
        } else {
            response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Handles exceptions that occur during servlet processing.
     *
     * @param response The Sling HTTP servlet response object.
     * @param e        The exception that occurred.
     * @throws IOException If an I/O error occurs.
     */
    private void handleException(SlingHttpServletResponse response, Exception e) throws IOException {
        response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(e.getMessage());
    }
}
