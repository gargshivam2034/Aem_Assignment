package com.aem.assignment.core.schedulers;

import com.aem.assignment.core.configs.PageConfig;
import com.aem.assignment.core.entities.ProductEntity;
import com.aem.assignment.core.services.PageService;
import com.aem.assignment.core.services.ProductDetailService;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobBuilder;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Component(service = {Runnable.class},immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = PageConfig.class)
public class PageScheduler implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(PageScheduler.class);
    private int schedulerId;

    @Reference
    ProductDetailService productDetailService;

    @Reference
    JobManager jobManager;

    @Reference
    PageService pageService;

    private static final String BASE_URL = "https://fakestoreapi.com/products/";

    private static String PRODUCT_ID = "1";

    public static String JOB_TOPIC = "training/pageJob";


    @Reference
    Scheduler scheduler;

    @Activate
    protected void active(PageConfig config){
        schedulerId = config.schedulerName().hashCode();
        addScheduler(config);
    }

    @Deactivate
    protected void deactivate(){
        removeScheduler();

    }

    public void addScheduler(PageConfig config){
        ScheduleOptions scheduleOptions = scheduler.EXPR(config.scheduler_expression());
        scheduleOptions .name(String.valueOf(schedulerId));
        //scheduleOptions.canRunConcurrently(false);
        scheduler.schedule(this,scheduleOptions);

    }

    protected void removeScheduler() {
        scheduler.unschedule(String.valueOf(schedulerId));
    }

    public void startJob(){
        Map<String,Object> propertyMap = new HashMap<>();
        propertyMap.put("name","PageScheduler");
        propertyMap.put("schedulerId",schedulerId);
        jobManager.addJob(JOB_TOPIC,propertyMap);
    }

    @Override
    public void run() {
        log.info("Starting the sling job......");
        startJob();
        log.debug("sling job has ended: ");
    }
}
