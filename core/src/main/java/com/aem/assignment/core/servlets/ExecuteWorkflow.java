package com.aem.assignment.core.servlets;
import com.adobe.granite.workflow.exec.Status;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowService;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(
        service = Servlet.class,
        property = {
                ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/workflow",
                ServletResolverConstants.SLING_SERVLET_METHODS + "=GET"
        }
)
public class ExecuteWorkflow extends SlingSafeMethodsServlet {

    private static final Logger LOGGER= LoggerFactory.getLogger(ExecuteWorkflow.class);


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        String data = request.getParameter("page");
        response.getWriter().write(data);
//        final ResourceResolver resourceResolver= request.getResourceResolver();
//        Status status;
//        WorkflowSession workflowSession= resourceResolver.adaptTo(WorkflowSession.class);
//        try{
//            WorkflowModel workflowModel = workflowSession != null ? workflowSession.getModel("/var/workflow/models/demo") : null;
//            assert workflowSession != null;
//            WorkflowData workflowData= workflowSession.newWorkflowData("JCR_PATH",payload);
//            status= Status.valueOf(workflowSession.startWorkflow(workflowModel,workflowData).getState());
//
//
//        } cat zch (WorkflowException e) {
//            throw new RuntimeException(e);
//        }
//        response.getWriter().write(status.toString());
//        logger.info("successfully");

    }
}
