package com.aem.assignment.core.services.impl;

import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.aem.assignment.core.entities.ProductEntity;
import com.aem.assignment.core.services.PageService;
import com.aem.assignment.core.services.SendNotificationService;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component(service = PageService.class)
public class PageServiceImpl implements PageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageServiceImpl.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private Replicator replicator;

    @Reference
    private MessageGatewayService messageGatewayService;

    @Reference
    private SendNotificationService notificationService;

    private static final String RECIPIENT_MAIL = "akash31aks@gmail.com";
    private static final String SERVICE_USER = "useruser";
    private static final String PARENT_PAGE_PATH = "/content/training-project/us";
    private static final String PAGE_TEMPLATE = "/conf/training-project/settings/wcm/templates/page-content";

    /**
     * Creates a new page based on the provided product entity details.
     *
     * @param productEntity the product entity containing page details.
     */
    @Override
    public void createPage(ProductEntity productEntity) {
        try (ResourceResolver resourceResolver = getResourceResolver()) {
            Session session = resourceResolver.adaptTo(Session.class);
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);

            Page createdPage = createPage(pageManager, productEntity);
            replicatePage(session, createdPage);
            sendMailToAdmin();
            notifyAdmin(resourceResolver);
        } catch (WCMException | LoginException e) {
            LOGGER.debug("Failed to create the page", e);
        }
    }

    /**
     * Retrieves a service resource resolver.
     *
     * @return a resource resolver.
     * @throws LoginException if login fails.
     */
    private ResourceResolver getResourceResolver() throws LoginException {
        Map<String, Object> map = new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER);
        return resourceResolverFactory.getServiceResourceResolver(map);
    }

    /**
     * Creates a new page.
     *
     * @param pageManager the page manager.
     * @param productEntity the product entity containing page details.
     * @return the created page.
     * @throws WCMException if page creation fails.
     */
    private Page createPage(PageManager pageManager, ProductEntity productEntity) throws WCMException {
        return pageManager.create(PARENT_PAGE_PATH, productEntity.getTitle(), PAGE_TEMPLATE, productEntity.getTitle(), true);
    }

    /**
     * Replicates the newly created page.
     *
     * @param session the JCR session.
     * @param page the page to replicate.
     */
    private void replicatePage(Session session, Page page) {
        try {
            replicator.replicate(session, ReplicationActionType.ACTIVATE, page.getPath());
        } catch (ReplicationException e) {
            LOGGER.debug("Failed to replicate the page", e);
        }
    }

    /**
     * Sends an email notification to the admin.
     */
    private void sendMailToAdmin() {
        try {
            MessageGateway<HtmlEmail> messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
            HtmlEmail email = composeEmail();
            messageGateway.send(email);
        } catch (EmailException e) {
            LOGGER.debug("Failed to send the Email to admin: {}", e);
        }
    }

    /**
     * Composes an email to be sent to the admin.
     *
     * @return the composed email.
     * @throws EmailException if email composition fails.
     */
    private HtmlEmail composeEmail() throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.addTo(RECIPIENT_MAIL);
        email.setSubject("Page Created By Scheduler at " + new Date().getTime());
        email.setTextMsg("A new page has been created by " + SERVICE_USER + " under " + PARENT_PAGE_PATH);
        return email;
    }

    /**
     * Sends a notification to the admin.
     *
     * @param resourceResolver the resource resolver.
     */
    private void notifyAdmin(ResourceResolver resourceResolver) {
        notificationService.setTaskNotification(resourceResolver, SERVICE_USER, PARENT_PAGE_PATH);
    }
}
