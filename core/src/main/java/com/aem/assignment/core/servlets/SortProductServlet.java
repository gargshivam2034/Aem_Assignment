package com.aem.assignment.core.servlets;

import com.google.gson.Gson;
import com.aem.assignment.core.entities.ProductEntity;
import com.aem.assignment.core.services.ProductDetailService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.eclipse.jetty.server.Server;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component(service = {Servlet.class},
        property = {
                "sling.servlet.paths=/bin/sortedProducts",
                "sling.servlet.methods=GET"
        })
public class SortProductServlet extends SlingSafeMethodsServlet {

    @Reference
    ProductDetailService productDetailService;

    private static final String MAIN_URL = "https://fakestoreapi.com/products";
    List<ProductEntity> productEntityList = new ArrayList<>();

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        productEntityList = productDetailService.getProductList(MAIN_URL);

        response.getWriter().write(new Gson().toJson(productEntityList));
    }
}
