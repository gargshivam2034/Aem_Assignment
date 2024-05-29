package com.aem.assignment.core.servlets;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.osgi.service.component.annotations.Component;

import javax.jcr.Node;
import javax.jcr.Session;

/**
 * Custom workflow process for updating node properties.
 */
@Component(service = WorkflowProcess.class, property = {
        "process.label=custom process"
})
public class CustomWorkflow implements WorkflowProcess {

    /**
     * Executes the custom workflow process.
     *
     * @param workItem      The work item being processed.
     * @param workflowSession The workflow session associated with the process.
     * @param metaDataMap   The metadata associated with the process.
     * @throws WorkflowException If an error occurs during workflow execution.
     */
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        try {
            WorkflowData workflowData = workItem.getWorkflowData();
            if (workflowData.getPayloadType().equals("JCR_PATH")) {
                Session session = workflowSession.adaptTo(Session.class);
                updateNodeProperties(session, workflowData, metaDataMap);
            }
        } catch (Exception ignored) {
            // Ignore exceptions for simplicity
        }
    }

    /**
     * Updates node properties based on the provided metadata map.
     *
     * @param session      The JCR session.
     * @param workflowData The workflow data containing the payload information.
     * @param metaDataMap  The metadata map containing property updates.
     * @throws Exception If an error occurs during property update.
     */
    private void updateNodeProperties(Session session, WorkflowData workflowData, MetaDataMap metaDataMap) throws Exception {
        String path = workflowData.getPayload() + "/jcr:content";
        Node node = session.getNode(path);
        String[] propertyUpdates = metaDataMap.get("PROCESS_ARGS", "string").split(",");
        for (String propertyUpdate : propertyUpdates) {
            String[] args = propertyUpdate.split(":");
            String propName = args[0];
            String propValue = args[1];
            if (node != null) {
                node.setProperty(propName, propValue);
            }
        }
        session.save();
    }
}
