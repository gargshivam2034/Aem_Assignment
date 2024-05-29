package com.aem.assignment.core.servlets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for demonstrating request parameter handling.
 */
@Component(service = Servlet.class)
@SlingServletResourceTypes(
        resourceTypes = "aem_assignment/components/page"
)
public class DemoServlet extends SlingSafeMethodsServlet {

    private static final Log log = LogFactory.getLog(DemoServlet.class);

    /**
     * Handles HTTP GET requests.
     *
     * @param slingHttpServletRequest  The Sling HTTP servlet request object.
     * @param slingHttpServletResponse The Sling HTTP servlet response object.
     * @throws ServletException If an error occurs during servlet processing.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doGet(final SlingHttpServletRequest slingHttpServletRequest, final SlingHttpServletResponse slingHttpServletResponse)
            throws ServletException, IOException {

        List<RequestParameter> requestParameterList = slingHttpServletRequest.getRequestParameterList();
        for (RequestParameter requestParameter : requestParameterList) {
            log.info(requestParameter.getString());
            slingHttpServletResponse.getWriter().write(requestParameter.getString());
        }
    }
}
