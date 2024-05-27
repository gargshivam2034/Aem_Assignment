package com.aem.assignment.core.servlets;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;
@Component(
        service = Servlet.class,
        property = {
                ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/demo",
                ServletResolverConstants.SLING_SERVLET_METHODS + "=GET,POST"
        }
)
public class DemoServletPathType extends SlingSafeMethodsServlet {
    private static final Logger logger = LoggerFactory.getLogger(DemoServletPathType.class);
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse slingHttpServletResponse)
            throws ServletException, IOException {
        ResourceResolver resourceResolver = request.getResourceResolver();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        if (pageManager == null) {
            return;
        }
        Page page = pageManager.getPage("/content/aem_assignment/us");
        JSONArray pageArray = new JSONArray();
        try {
            pageManager.create("","","","");
            Iterator<Page> iterator = page.listChildren();
            while (iterator.hasNext()) {
                Page childPage = iterator.next();
                JSONObject pageObject = new JSONObject();
                pageObject.put("title", childPage.getTitle());
                pageArray.put(pageObject);
            }


        } catch (Exception e) {
            logger.error("Error occurred while retrieving pages: {}", e.getMessage());
        }
        slingHttpServletResponse.setContentType("application/json");
        slingHttpServletResponse.getWriter().write(pageArray.toString());
    }

}
