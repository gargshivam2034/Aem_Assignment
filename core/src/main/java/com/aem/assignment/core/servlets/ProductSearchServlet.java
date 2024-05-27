package com.aem.assignment.core.servlets;

import com.aem.assignment.core.services.ProductSearchService;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

@Component(
        service = { Servlet.class },
        property = {
                "sling.servlet.paths=/bin/productsearch",
                "sling.servlet.methods=GET"
        }
)
public class ProductSearchServlet extends SlingAllMethodsServlet {

    @Reference
    private transient ProductSearchService productSearchService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        List<String> results = productSearchService.searchProducts(query, request.getResourceResolver());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(results));
    }
}
