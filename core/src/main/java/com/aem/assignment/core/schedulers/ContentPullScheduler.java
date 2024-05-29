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

/**
 * A scheduler in AEM to periodically pull content from a third-party API and create pages.
 */
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

    /**
     * Activate the scheduler to run the job at defined intervals.
     */
    @Activate
    protected void activate() {
        scheduleJob();
    }

    /**
     * The main run method for the scheduler.
     */
    @Override
    public void run() {
        try (ResourceResolver resourceResolver = getServiceResourceResolver()) {
            if (resourceResolver != null) {
                handleContentPull(resourceResolver);
            }
        } catch (Exception e) {
            LOG.error("Error occurred while running the scheduler", e);
        }
    }

    /**
     * Schedule the job with specified cron expression.
     */
    private void scheduleJob() {
        ScheduleOptions options = scheduler.EXPR("0/6 * * * * ?"); // Every 6 seconds
        options.name("Content Pull Scheduler - Every 6 Seconds");
        options.canRunConcurrently(false);
        scheduler.schedule(this, options);
        LOG.info("Content Pull Scheduler activated.");
    }

    /**
     * Handles the process of pulling content from the API and creating a page.
     *
     * @param resourceResolver the ResourceResolver instance
     * @throws Exception if an error occurs during the process
     */
    private void handleContentPull(ResourceResolver resourceResolver) throws Exception {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        if (pageManager != null) {
            String content = fetchContentFromAPI();
            createAndReplicatePage(resourceResolver, pageManager, content);
        }
    }

    /**
     * Fetches content from the specified API URL.
     *
     * @return the content as a String
     */
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

    /**
     * Creates a new page with the fetched content and replicates it.
     *
     * @param resourceResolver the ResourceResolver instance
     * @param pageManager      the PageManager instance
     * @param content          the content to be added to the page
     */
    private void createAndReplicatePage(ResourceResolver resourceResolver, PageManager pageManager, String content) {
        try {
            String pageTitle = "New Page Title";
            Page newPage = pageManager.create(PARENT_PATH, "pageTitle", TEMPLATE_PATH, "pageTitle");

            if (newPage != null) {
                Node contentNode = getContentNode(newPage);
                if (contentNode != null) {
                    populateContentNode(contentNode, pageTitle, content);
                    replicatePage(resourceResolver, newPage.getPath());
                    logAudit(contentNode, "Page created and replicated");
                }
            }
        } catch (RepositoryException | ReplicationException | WCMException e) {
            LOG.error("Error creating or replicating the page", e);
        }
    }

    /**
     * Retrieves the jcr:content node of the newly created page.
     *
     * @param newPage the newly created Page instance
     * @return the jcr:content Node, or null if not found
     * @throws RepositoryException if an error occurs while retrieving the node
     */
    private Node getContentNode(Page newPage) throws RepositoryException {
        Node pageNode = newPage.adaptTo(Node.class);
        if (pageNode != null && pageNode.hasNode("jcr:content")) {
            return pageNode.getNode("jcr:content");
        }
        return null;
    }

    /**
     * Populates the content node with the given title and content.
     *
     * @param contentNode the content Node
     * @param pageTitle   the title of the page
     * @param content     the content to be added
     * @throws RepositoryException if an error occurs while setting properties
     */
    private void populateContentNode(Node contentNode, String pageTitle, String content) throws RepositoryException {
        contentNode.setProperty("jcr:title", pageTitle);
        contentNode.setProperty("myproject:content", content);
        contentNode.getSession().save();
    }

    /**
     * Replicates the created page.
     *
     * @param resourceResolver the ResourceResolver instance
     * @param pagePath         the path of the page to be replicated
     * @throws ReplicationException if an error occurs during replication
     */
    private void replicatePage(ResourceResolver resourceResolver, String pagePath) throws ReplicationException {
        replicator.replicate(resourceResolver.adaptTo(Session.class), ReplicationActionType.ACTIVATE, pagePath);
    }

    /**
     * Logs an audit message in the content node.
     *
     * @param node    the content Node
     * @param message the audit message
     * @throws RepositoryException if an error occurs while logging the audit
     */
    private void logAudit(Node node, String message) throws RepositoryException {
        if (node != null) {
            Node auditNode = getAuditNode(node);
            Node logNode = auditNode.addNode("log" + System.currentTimeMillis(), "nt:unstructured");
            logNode.setProperty("message", message);
            logNode.setProperty("timestamp", System.currentTimeMillis());
            node.getSession().save();
        }
    }

    /**
     * Retrieves or creates the auditLogs node.
     *
     * @param node the parent Node
     * @return the auditLogs Node
     * @throws RepositoryException if an error occurs while retrieving or creating the node
     */
    private Node getAuditNode(Node node) throws RepositoryException {
        if (!node.hasNode("auditLogs")) {
            return node.addNode("auditLogs", "nt:unstructured");
        }
        return node.getNode("auditLogs");
    }

    /**
     * Obtains a service ResourceResolver.
     *
     * @return the ResourceResolver instance, or null if an error occurs
     */
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
