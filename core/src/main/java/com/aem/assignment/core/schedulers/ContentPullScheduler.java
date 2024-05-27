package com.aem.assignment.core.schedulers;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component(immediate = true, service = Runnable.class)
public class ContentPullScheduler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ContentPullScheduler.class);
    private static final String TEMPLATE_PATH = "/conf/aem_assignment/settings/wcm/templates/aem-practice";
    private static final String PARENT_PATH = "/content/aem_assignment/us/en";
    private static final String API_URL = "https://fakestoreapi.com/products/2";

    @Reference
    private Scheduler scheduler;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private Replicator replicator;

    @Activate
    protected void activate() {
        ScheduleOptions options = scheduler.EXPR("0/6 * * * * ?"); // Every 6 seconds
        options.name("Content Pull Scheduler - Every 15 Minutes");
        options.canRunConcurrently(false);
        scheduler.schedule(this, options);
        LOG.info("Content Pull Scheduler activated.");
    }

    @Override
    public void run() {
        try (ResourceResolver resourceResolver = getServiceResourceResolver()) {
            if (resourceResolver != null) {
                PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
                if (pageManager != null) {
                    // Pull content from third-party API
                    String content = fetchContentFromAPI();

                    // Process and create pages based on the fetched content
                    createAndReplicatePage(resourceResolver, pageManager, content);
                }
            }
        } catch (Exception e) {
            LOG.error("Error occurred while running the scheduler", e);
        }
    }

    private String fetchContentFromAPI() {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
        } catch (Exception e) {
            LOG.error("Error fetching content from API", e);
        }
        return content.toString();
    }

    private void createAndReplicatePage(ResourceResolver resourceResolver, PageManager pageManager, String content) {
        try {
            String pageTitle = "New Page Title";
            Page newPage = pageManager.create(PARENT_PATH, "pageTitle", TEMPLATE_PATH, "pageTitle");

            if (newPage != null) {
                Node pageNode = newPage.adaptTo(Node.class);
                if (pageNode != null && pageNode.hasNode("jcr:content")) {
                    Node contentNode = pageNode.getNode("jcr:content");
                    contentNode.setProperty("jcr:title", pageTitle);
                    contentNode.setProperty("myproject:content", content);
                    contentNode.getSession().save();
                    replicator.replicate(resourceResolver.adaptTo(Session.class), ReplicationActionType.ACTIVATE, newPage.getPath());


                    logAudit(contentNode, "Page created and replicated");
                }
            }
        } catch (RepositoryException | ReplicationException e) {
            LOG.error("Error creating or replicating the page", e);
        } catch (WCMException e) {
            throw new RuntimeException(e);
        }
    }

    private void logAudit(Node node, String message) throws RepositoryException {
        if (node != null) {
            Node auditNode;
            if (!node.hasNode("auditLogs")) {
                auditNode = node.addNode("auditLogs", "nt:unstructured");
            } else {
                auditNode = node.getNode("auditLogs");
            }
            Node logNode = auditNode.addNode("log" + System.currentTimeMillis(), "nt:unstructured");
            logNode.setProperty("message", message);
            logNode.setProperty("timestamp", System.currentTimeMillis());
            node.getSession().save();
        }
    }

    private ResourceResolver getServiceResourceResolver() {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put(ResourceResolverFactory.SUBSERVICE, "demouser");
            return resourceResolverFactory.getServiceResourceResolver(param);
        } catch (LoginException e) {
            LOG.error("Error obtaining service resource resolver", e);
        }
        return null;
    }
}
