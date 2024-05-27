package com.aem.assignment.core.services.impl;

import com.adobe.granite.taskmanagement.Task;
import com.adobe.granite.taskmanagement.TaskManager;
import com.adobe.granite.taskmanagement.TaskManagerException;
import com.adobe.granite.taskmanagement.TaskManagerFactory;
import com.adobe.granite.workflow.exec.InboxItem;
import com.aem.assignment.core.services.SendNotificationService;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = SendNotificationService.class)
public class SendNotificationServiceImpl implements SendNotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendNotificationServiceImpl.class);

    @Override
    public void setTaskNotification(ResourceResolver resourceResolver,String createdBy, String pagePath) {

        try {
            TaskManager taskManager = resourceResolver.adaptTo(TaskManager.class);
            TaskManagerFactory taskManagerFactory = taskManager.getTaskManagerFactory();
            Task newTask = taskManagerFactory.newTask("Notification");
            newTask.setName("New Page Created Notification");
            newTask.setContentPath(pagePath); // Set the path of the newly created page
            newTask.setPriority(InboxItem.Priority.HIGH);
            newTask.setDescription("A new page has been created");
            newTask.setInstructions("Review the newly created page");
            newTask.setCurrentAssignee("admin"); // Assign the task to the admin
            taskManager.createTask(newTask);
        } catch (TaskManagerException e) {
            LOGGER.debug("Failed to send notification to admin: {} ",e);
        }

    }
}
