package com.aem.assignment.core.servlets;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.Status;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.model.WorkflowModel;
import com.day.cq.workflow.WorkflowException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = Servlet.class,
property = {
        ServletResolverConstants.SLING_SERVLET_PATHS+ "=" + "/bin/testing"
})
public class TestServlet  extends SlingSafeMethodsServlet {

    private static final Logger LOGGER= LoggerFactory.getLogger(TestServlet.class);


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String payload = request.getParameter("page");
        String status;
        try {
            ResourceResolver resourceResolver = request.getResourceResolver();

             WorkflowSession workflowSession = resourceResolver.adaptTo(WorkflowSession.class);
            if(workflowSession==null) return;
            WorkflowModel workflowModel =  workflowSession.getModel("/var/workflow/models/demo");
            WorkflowData workflowData = workflowSession.newWorkflowData("JCR_PATH",payload);
            Workflow workflow = workflowSession.startWorkflow(workflowModel, workflowData);
            status = workflow.getState();

        } catch (com.adobe.granite.workflow.WorkflowException e) {
            throw new RuntimeException(e);
        }

        response.getWriter().write(status);
        LOGGER.info("successfully");
    }
}
