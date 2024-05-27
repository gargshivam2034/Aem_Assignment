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

@Component(service = Runnable.class,immediate = true)
@Designate(ocd= SchedulerForFetchingData.Config.class)
public class SchedulerForFetchingData implements Runnable {
    @ObjectClassDefinition(name="A scheduled task for fetching data",
            description = "Simple demo for cron-job like task with properties")
    public static @interface Config {

        @AttributeDefinition(name = "Cron-job expression")
        String scheduler_expression() default "*/5 * * * *";

        @AttributeDefinition(name = "Concurrent task",
                description = "Whether or not to schedule this task concurrently")
        boolean scheduler_concurrent() default false;

        @AttributeDefinition(name = "A parameter",
                description = "Can be configured in /system/console/configMgr")
        String myParameter() default "";
    }
    @Reference
    private ReplicatingAgentService replicatingAgentService;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public void run() {
       String string=replicatingAgentService.ApiFetchData();

        try {
            Map<String,Object> authMap=new HashMap<>();
            authMap.put(ResourceResolverFactory.SUBSERVICE,"customuser");
            ResourceResolver resourceResolver=resourceResolverFactory.getResourceResolver(authMap);
            JSONObject jsonObject=new JSONObject(string);
            jsonObject.get("id");
            replicatingAgentService.CreatePage(resourceResolver);

        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }

    }


}
