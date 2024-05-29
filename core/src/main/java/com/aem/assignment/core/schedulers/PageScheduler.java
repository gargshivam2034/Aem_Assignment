package com.aem.assignment.core.schedulers;

import com.aem.assignment.core.configs.PageConfig;
import com.aem.assignment.core.services.PageService;
import com.aem.assignment.core.services.ProductDetailService;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Scheduler component for pulling product details and creating pages in AEM.
 */
@Component(service = {Runnable.class}, immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = PageConfig.class)
public class PageScheduler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(PageScheduler.class);

    @Reference
    private ProductDetailService productDetailService;

    @Reference
    private JobManager jobManager;

    @Reference
    private PageService pageService;

    @Reference
    private Scheduler scheduler;

    private int schedulerId;
    private static final String BASE_URL = "https://fakestoreapi.com/products/";
    private static final String PRODUCT_ID = "1";
    private static final String JOB_TOPIC = "training/pageJob";

    /**
     * Activate the scheduler with the provided configuration.
     *
     * @param config the PageConfig instance containing scheduler configuration
     */
    @Activate
    protected void activate(PageConfig config) {
        schedulerId = config.schedulerName().hashCode();
        addScheduler(config);
    }

    /**
     * Deactivate the scheduler.
     */
    @Deactivate
    protected void deactivate() {
        removeScheduler();
    }

    /**
     * Add and schedule the job with the given configuration.
     *
     * @param config the PageConfig instance containing scheduler configuration
     */
    public void addScheduler(PageConfig config) {
        ScheduleOptions scheduleOptions = scheduler.EXPR(config.scheduler_expression());
        scheduleOptions.name(String.valueOf(schedulerId));
        scheduler.schedule(this, scheduleOptions);
    }

    /**
     * Remove the scheduler.
     */
    protected void removeScheduler() {
        scheduler.unschedule(String.valueOf(schedulerId));
    }

    /**
     * Start the job to create pages based on the product details.
     */
    public void startJob() {
        Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put("name", "PageScheduler");
        propertyMap.put("schedulerId", schedulerId);
        jobManager.addJob(JOB_TOPIC, propertyMap);
    }

    /**
     * Run method executed by the scheduler.
     */
    @Override
    public void run() {
        log.info("Starting the sling job...");
        startJob();
        log.debug("Sling job has ended.");
    }
}
