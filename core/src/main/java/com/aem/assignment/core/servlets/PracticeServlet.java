package com.aem.assignment.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for practicing various operations such as node removal and property setting.
 */
@Component(service = Servlet.class,
        property = {
                ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/meno",
                ServletResolverConstants.SLING_SERVLET_METHODS + "=GET,POST"
        }
)
public class PracticeServlet extends SlingAllMethodsServlet {

    /**
     * Handles HTTP GET requests to remove nodes specified in the request parameters.
     *
     * @param request  The Sling HTTP servlet request object.
     * @param response The Sling HTTP servlet response object.
     * @throws ServletException If an error occurs during servlet processing.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        Logger logger = LoggerFactory.getLogger(PracticeServlet.class);
        Session session = getSession(request);
        List<RequestParameter> requestParameterList = request.getRequestParameterList();
        Node rootNode = null;
        try {
            rootNode = getRootNode(session);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        try {
            for (RequestParameter requestParameter : requestParameterList) {
                String path = rootNode.getPath() + requestParameter.getString();
                removeNode(session, path);
            }
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        logger.error("Successfully removed nodes");
    }

    /**
     * Handles HTTP POST requests to set a property on a specific node.
     *
     * @param request  The Sling HTTP servlet request object.
     * @param response The Sling HTTP servlet response object.
     * @throws ServletException If an error occurs during servlet processing.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        Node node = request.getResourceResolver().getResource("/var/dam").adaptTo(Node.class);
        try {
            setProperty(node);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        response.getWriter().write("Property is set");
    }

    private Session getSession(SlingHttpServletRequest request) {
        return request.getResourceResolver().adaptTo(Session.class);
    }

    private Node getRootNode(Session session) throws RepositoryException {
        return session.getRootNode();
    }

    private void removeNode(Session session, String path) throws RepositoryException {
        Node node = session.getNode(path);
        node.remove();
        session.save();
    }

    private void setProperty(Node node) throws RepositoryException {
        node.setProperty("propertyValue", "property_Name");
    }
}
