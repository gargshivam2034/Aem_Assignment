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

@Component(service = Servlet.class, property = {
        ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/myServlet",
        ServletResolverConstants.SLING_SERVLET_METHODS + "=GET,POST"
})
public class AjaxServlet extends SlingAllMethodsServlet {

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            String requestBodyString = extractRequestBody(request);
            JSONObject jsonObject = new JSONObject(requestBodyString);
            response.setContentType("text/plain");
            response.getWriter().write(requestBodyString);

            Session session = request.getResourceResolver().adaptTo(Session.class);
            if (session != null) {
                updateNodeProperties(session, jsonObject);
                session.save();
            } else {
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Session could not be adapted.");
            }
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    /**
     * Extracts the request body from the HTTP request.
     *
     * @param request the HTTP request
     * @return the request body as a string
     * @throws IOException if an I/O error occurs
     */
    private String extractRequestBody(SlingHttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        StringBuilder requestBody = new StringBuilder();
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            requestBody.append(new String(buffer, 0, bytesRead));
        }
        inputStream.close();
        return requestBody.toString();
    }

    /**
     * Updates the node properties with values from the JSON object.
     *
     * @param session   the JCR session
     * @param jsonObject the JSON object containing the properties
     * @throws Exception if an error occurs while updating the node
     */
    private void updateNodeProperties(Session session, JSONObject jsonObject) throws Exception {
        Node node = session.getNode("/content/aem_assignment/jcr:content");
        node.setProperty("username", jsonObject.getString("username"));
        node.setProperty("password", jsonObject.getString("password"));
    }

    /**
     * Handles errors by setting the HTTP response status and writing the error message.
     *
     * @param response the HTTP response
     * @param e the exception that occurred
     * @throws IOException if an I/O error occurs
     */
    private void handleError(SlingHttpServletResponse response, Exception e) throws IOException {
        response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("Error occurred: " + e.getMessage());
    }
}
