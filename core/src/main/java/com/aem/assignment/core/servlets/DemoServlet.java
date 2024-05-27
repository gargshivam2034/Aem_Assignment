package com.aem.assignment.core.servlets;

import com.adobe.cq.wcm.core.components.models.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.slf4j.ILoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

@Component(service = Servlet.class)
@SlingServletResourceTypes(
        resourceTypes = "aem_assignment/components/page"
)
public class DemoServlet extends SlingSafeMethodsServlet {


    private static final Log log= LogFactory.getLog(DemoServlet.class);
    @Override
    protected void doGet(final SlingHttpServletRequest slingHttpServletRequest,final SlingHttpServletResponse slingHttpServletResponse)
            throws ServletException, IOException {

        List<RequestParameter>  requestParameterList=slingHttpServletRequest.getRequestParameterList();
        for(RequestParameter requestParameter:requestParameterList) {
            log.info(requestParameter.getString());
            slingHttpServletResponse.getWriter().write(requestParameter.getString());
        }

    }
}
