package com.aem.assignment.core.schedulers;

import com.aem.assignment.core.services.ReplicatingAgentService;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * A scheduled task for fetching data from a third-party API and creating pages in AEM.
 */
@Component(service = Runnable.class, immediate = true)
@Designate(ocd = SchedulerForFetchingData.Config.class)
public class SchedulerForFetchingData implements Runnable {

    /**
     * OSGi configuration interface for the scheduler.
     */
    @ObjectClassDefinition(
            name = "A scheduled task for fetching data",
            description = "Simple demo for cron-job like task with properties"
    )
    public static @interface Config {

        /**
         * Cron-job expression for scheduling.
         *
         * @return the cron expression as a string
         */
        @AttributeDefinition(name = "Cron-job expression")
        String scheduler_expression() default "*/5 * * * *";

        /**
         * Indicates if the task should run concurrently.
         *
         * @return true if concurrent, false otherwise
         */
        @AttributeDefinition(
                name = "Concurrent task",
                description = "Whether or not to schedule this task concurrently"
        )
        boolean scheduler_concurrent() default false;

        /**
         * A configurable parameter.
         *
         * @return the parameter as a string
         */
        @AttributeDefinition(
                name = "A parameter",
                description = "Can be configured in /system/console/configMgr"
        )
        String myParameter() default "";
    }

    @Reference
    private ReplicatingAgentService replicatingAgentService;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    /**
     * Fetches data from the API and creates a page in AEM based on the data.
     */
    @Override
    public void run() {
        String responseData = replicatingAgentService.ApiFetchData();

        try {
            Map<String, Object> authMap = new HashMap<>();
            authMap.put(ResourceResolverFactory.SUBSERVICE, "customuser");
            ResourceResolver resourceResolver = resourceResolverFactory.getResourceResolver(authMap);

            JSONObject jsonObject = new JSONObject(responseData);
            jsonObject.get("id");  // Process the JSON data as needed

            replicatingAgentService.CreatePage(resourceResolver);
        } catch (JSONException e) {
            throw new RuntimeException("Error parsing JSON data from API", e);
        } catch (LoginException e) {
            throw new RuntimeException("Error obtaining resource resolver", e);
        }
    }
}
