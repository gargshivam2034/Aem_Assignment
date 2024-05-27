package com.aem.assignment.core.servlets;
import org.apache.lucene.analysis.hunspell.HunspellStemmer;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
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

@Component(service = Servlet.class,
        property = {
        ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/meno",
        ServletResolverConstants.SLING_SERVLET_METHODS + "=GET,POST"
        }
)
public class PracticeServlet extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        Logger logger = LoggerFactory.getLogger( PracticeServlet.class);
        Session session = request.getResourceResolver().adaptTo(Session.class);
        Node node;
        try {
           List<RequestParameter> requestParameterList= request.getRequestParameterList();
            Node rootNode=session.getRootNode();
            String string=rootNode.getPath();
            for(RequestParameter requestParameter:requestParameterList)
            {
                String string2=requestParameter.getString();
               String string1=string+string2;
               logger.error(string1);
                node = session.getNode(string1);
                node.remove();
                session.save();
            }
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }

        logger.error("successfull ");

    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        Node node = request.getResourceResolver().getResource("/var/dam").adaptTo(Node.class);
        String string;
        try {
            node.setProperty("propertyValue","property_Name");
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        response.getWriter().write("Propertyis set");
    }
}
