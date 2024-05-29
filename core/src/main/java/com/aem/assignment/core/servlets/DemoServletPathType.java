package com.aem.assignment.core.servlets;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;

/**
 * Servlet for retrieving child pages under a specific path and returning them as JSON.
 */
@Component(
        service = Servlet.class,
        property = {
                ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/demo",
                ServletResolverConstants.SLING_SERVLET_METHODS + "=GET,POST"
        }
)
public class DemoServletPathType extends SlingSafeMethodsServlet {

    private static final Logger logger = LoggerFactory.getLogger(DemoServletPathType.class);

    /**
     * Handles HTTP GET requests to retrieve child pages and return them as JSON.
     *
     * @param request  The Sling HTTP servlet request object.
     * @param response The Sling HTTP servlet response object.
     * @throws ServletException If an error occurs during servlet processing.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        ResourceResolver resourceResolver = request.getResourceResolver();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        if (pageManager != null) {
            Page page = getPage(pageManager);
            if (page != null) {
                JSONArray pageArray = retrieveChildPages(page);
                writeResponse(response, pageArray);
            }
        }
    }

    /**
     * Retrieves the page at the specified path.
     *
     * @param pageManager The PageManager instance.
     * @return The Page object or null if not found.
     */
    private Page getPage(PageManager pageManager) {
        return pageManager.getPage("/content/aem_assignment/us");
    }

    /**
     * Retrieves child pages of the given page and constructs a JSONArray containing their titles.
     *
     * @param page The parent page.
     * @return JSONArray containing child page titles.
     */
    private JSONArray retrieveChildPages(Page page) {
        JSONArray pageArray = new JSONArray();
        try {
            Iterator<Page> iterator = page.listChildren();
            while (iterator.hasNext()) {
                Page childPage = iterator.next();
                JSONObject pageObject = new JSONObject();
                pageObject.put("title", childPage.getTitle());
                pageArray.put(pageObject);
            }
        } catch (JSONException e) {
            logger.error("Error occurred while constructing JSON response: {}", e.getMessage());
        }
        return pageArray;
    }

    /**
     * Writes the JSON response to the servlet response.
     *
     * @param response  The Sling HTTP servlet response object.
     * @param pageArray The JSONArray to be written as the response.
     * @throws IOException If an I/O error occurs.
     */
    private void writeResponse(SlingHttpServletResponse response, JSONArray pageArray) throws IOException {
        response.setContentType("application/json");
        response.getWriter().write(pageArray.toString());
    }
}
