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
    ResourceResolverFactory resourceResolverFactory;

    @Reference
    Replicator replicator;

    @Reference
    MessageGatewayService messageGatewayService;

    @Reference
    SendNotificationService notificationService;

    private static final String RECIPIENT_MAIL = "akash31aks@gmail.com";
    private static final String SERVICE_USER = "useruser";


    private static final String PARENT_PAGE_PATH = "/content/training-project/us";

    private static final String PAGE_TEMPLATE = "/conf/training-project/settings/wcm/templates/page-content";

    @Override
    public void createPage(ProductEntity productEntity) {


        try (ResourceResolver resourceResolver = getResourceResolver()){

            Session session = resourceResolver.adaptTo(Session.class);

            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            Page createdPage = pageManager.create(PARENT_PAGE_PATH,
                    productEntity.getTitle(),PAGE_TEMPLATE,
                    productEntity.getTitle(),true);
            LOGGER.debug("Newly created page: {}", createdPage);
            replicatePage(session,createdPage);
            sendMailToAdmin();
            notificationService.setTaskNotification(resourceResolver,SERVICE_USER,PARENT_PAGE_PATH);
        } catch (WCMException | LoginException e) {
            LOGGER.debug("Failed to create the page",e);
        }


    }

    private ResourceResolver getResourceResolver() throws LoginException {
        Map<String, Object> map = new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER);
        return resourceResolverFactory.getServiceResourceResolver(map);
    }

    private void replicatePage(Session session, Page page) {
        try {
            replicator.replicate(session, ReplicationActionType.ACTIVATE, page.getPath());
        } catch (ReplicationException e) {
            LOGGER.debug("Failed to replicate the page",e);
        }
    }



    private void sendMailToAdmin(){
        try {
            MessageGateway<HtmlEmail> messageGateway = messageGatewayService.getGateway(HtmlEmail.class);

            Date date = new Date();

            HtmlEmail email = new HtmlEmail();
            email.addTo(RECIPIENT_MAIL);
            email.setSubject("Page Created By Scheduler at "+ date.getTime() );
            email.setTextMsg("A new page has been created by"
                    + SERVICE_USER + "under"
                    + PARENT_PAGE_PATH);

            messageGateway.send(email);

        } catch (EmailException e) {
            LOGGER.debug("Failed to send the Email to admin: {}", e);
        }
    }

    private void notificationTOAdmin(){

    }

}
