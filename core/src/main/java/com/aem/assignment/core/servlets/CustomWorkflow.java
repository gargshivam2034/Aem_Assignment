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

@Component(service = WorkflowProcess.class, property = {
        "process.label=custom process"
})
public class CustomWorkflow implements WorkflowProcess {

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        try {
            WorkflowData workflowData = workItem.getWorkflowData();
            if(workflowData.getPayloadType().equals("JCR_PATH")) {
                Session session = workflowSession.adaptTo(Session.class);
                String path = workflowData.getPayload() + "/jcr:content";
                assert session != null;
                Node node = session.getNode(path);
                String[] strings= metaDataMap.get("PROCESS_ARGS","string").split(",");
                for(String processArg:strings)
                {
                    String[] args=processArg.split(":");
                    String  prop=args[0];
                    String value=args[1];
                    if(node!=null)
                    {
                        node.setProperty(prop,value);
                    }
                }
                session.save();
            }
        }
        catch (Exception ignored)
        {

        }
    }
}
