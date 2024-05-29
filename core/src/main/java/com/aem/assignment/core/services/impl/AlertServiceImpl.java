package com.aem.assignment.core.services.impl;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.aem.assignment.core.entities.AlertContentFragmentEntity;
import com.aem.assignment.core.services.AlertService;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the AlertService interface which retrieves alert content from Content Fragments.
 */
@Component(service = AlertService.class)
public class AlertServiceImpl implements AlertService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertServiceImpl.class);

    /**
     * Path to the folder containing Content Fragments.
     */
    public static final String CF_PATH = "/content/dam/aem-assignment/content-fragments";

    /**
     * Name of the element representing the alert in the Content Fragment.
     */
    private static final String ALERT = "alert";

    /**
     * Name of the element representing the message in the Content Fragment.
     */
    private static final String MESSAGE = "message";

    /**
     * Service user used for accessing resources.
     */
    private static final String SERVICE_USER = "useruser";

    /**
     * Resource resolver factory reference.
     */
    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    /**
     * Retrieves alerts from Content Fragments.
     *
     * @return List of AlertContentFragmentEntity objects representing the alerts.
     */
    public List<AlertContentFragmentEntity> getAlertsFromContentFragment() {
        List<AlertContentFragmentEntity> fragmentEntities = new ArrayList<>();
        try (ResourceResolver resourceResolver = getResourceResolver()) {
            Resource contentFragmentsFolder = getContentFragmentsFolder(resourceResolver);
            processContentFragments(contentFragmentsFolder, fragmentEntities);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        LOGGER.debug("List That will be rendered to client side : {}", fragmentEntities);
        return fragmentEntities;
    }

    /**
     * Retrieves a resource resolver with the appropriate permissions.
     *
     * @return ResourceResolver instance.
     * @throws LoginException If unable to log in.
     */
    private ResourceResolver getResourceResolver() throws LoginException {
        Map<String, Object> map = new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER);
        return resourceResolverFactory.getServiceResourceResolver(map);
    }

    /**
     * Retrieves the folder containing Content Fragments.
     *
     * @param resourceResolver ResourceResolver instance.
     * @return Resource representing the Content Fragments folder.
     */
    private Resource getContentFragmentsFolder(ResourceResolver resourceResolver) {
        return resourceResolver.getResource(CF_PATH);
    }

    /**
     * Processes the Content Fragments in the given folder.
     *
     * @param contentFragmentsFolder Resource representing the folder containing Content Fragments.
     * @param fragmentEntities        List to store the processed AlertContentFragmentEntity objects.
     */
    private void processContentFragments(Resource contentFragmentsFolder, List<AlertContentFragmentEntity> fragmentEntities) {
        LOGGER.debug("Content Fragment folder from path: {}", contentFragmentsFolder);
        if (contentFragmentsFolder != null && contentFragmentsFolder.hasChildren()) {
            Iterable<Resource> children = contentFragmentsFolder.getChildren();
            for (Resource child : children) {
                if (!"jcr:content".equals(child.getName())) {
                    processContentFragment(child, fragmentEntities);
                }
            }
        }
    }

    /**
     * Processes a single Content Fragment.
     *
     * @param contentFragmentResource Resource representing the Content Fragment.
     * @param fragmentEntities        List to store the processed AlertContentFragmentEntity objects.
     */
    private void processContentFragment(Resource contentFragmentResource, List<AlertContentFragmentEntity> fragmentEntities) {
        AlertContentFragmentEntity alertContentFragmentEntity = new AlertContentFragmentEntity();
        ContentFragment contentFragment = contentFragmentResource.adaptTo(ContentFragment.class);
        LOGGER.debug("Content Fragment from the above folder: {}", contentFragment);
        String alert = contentFragment.getElement(ALERT).getContent();
        String message = contentFragment.getElement(MESSAGE).getContent();
        String name = contentFragment.getName();

        alertContentFragmentEntity.setName(name);
        alertContentFragmentEntity.setAlert(alert);
        alertContentFragmentEntity.setMessage(message);
        LOGGER.debug("Final entity to be added in fragmentEntities List: {}",
                alertContentFragmentEntity);
        fragmentEntities.add(alertContentFragmentEntity);
    }
}
