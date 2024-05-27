package com.aem.assignment.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;

@Component(service = Servlet.class,property =
        {
        ServletResolverConstants.SLING_SERVLET_PATHS+"=/bin/myServlet",
        ServletResolverConstants.SLING_SERVLET_METHODS+"=GET,POST"
        })
     public class AjaxServlet extends SlingAllMethodsServlet {
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try{
        InputStream inputStream = request.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        StringBuilder requestBody = new StringBuilder();
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            requestBody.append(new String(buffer, 0, bytesRead));
        }
        inputStream.close();
         String requestBodyString = requestBody.toString();
            JSONObject jsonObject = new JSONObject(requestBodyString);
         response.setContentType("text/plain");
         response.getWriter().write(requestBodyString);
         Session session= request.getResourceResolver().adaptTo(Session.class);
         Node node=session.getNode("/content/aem_assignment/jcr:content");

       node.setProperty("username",(String)jsonObject.get("username"));
       node.setProperty("password",(String)jsonObject.get("password"));
       session.save();
      }
        catch (Exception e)
        {
        response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("Error occurred: " + e.getMessage());
      }
    }
}
