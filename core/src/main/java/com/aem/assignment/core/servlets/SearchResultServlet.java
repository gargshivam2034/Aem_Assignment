package com.aem.assignment.core.servlets;


import com.aem.assignment.core.services.QuerySearchService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class)
@SlingServletPaths(
        value = "/bin/demo servlet"
)
public class SearchResultServlet extends SlingSafeMethodsServlet {
    @Reference
    QuerySearchService querySearchService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        JSONObject jsonObject = new JSONObject();
        try {
            String searchText = "women";
            jsonObject = querySearchService.getSearchResult(searchText, request.getResourceResolver());
        } catch (LoginException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}