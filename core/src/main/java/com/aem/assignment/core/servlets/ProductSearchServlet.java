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

/**
 * Servlet responsible for handling product search requests.
 */
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

    /**
     * Handles HTTP GET requests for product search.
     *
     * @param request  The Sling HTTP servlet request object.
     * @param response The Sling HTTP servlet response object.
     * @throws ServletException If an error occurs during servlet processing.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        List<String> results = productSearchService.searchProducts(query, request.getResourceResolver());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(results));
    }
}
