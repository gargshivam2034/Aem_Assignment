package com.aem.assignment.core.services.impl;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.aem.assignment.core.entities.AlertContentFragmentEntity;
import com.aem.assignment.core.services.AlertService;
import com.aem.assignment.core.services.PageService;
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

@Component(service = AlertService.class )
public class AlertServiceImpl implements AlertService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertServiceImpl.class);

    public static final String CF_PATH = "/content/dam/training-project/content-fragments";
    private static final String ALERT = "alert";
    private static final String MESSAGE = "message";
    private static final String SERVICE_USER = "useruser";

    @Reference
    ResourceResolverFactory resourceResolverFactory;


    public List<AlertContentFragmentEntity> getAlertsFromContentFragment(){
        List<AlertContentFragmentEntity> fragmentEntities = new ArrayList<>();
        try(ResourceResolver resourceResolver = getResourceResolver()){
            Resource contentFragmentsFolder = resourceResolver.getResource(CF_PATH);
            LOGGER.debug("Content Fragment folder from path: {}", contentFragmentsFolder);
            if(contentFragmentsFolder.hasChildren() && contentFragmentsFolder!=null){
                    Iterable<Resource> children = contentFragmentsFolder.getChildren();
                    for(Resource child:children){
                        if("jcr:content".equals(child.getName())){
                            continue;
                        }

                        AlertContentFragmentEntity alertContentFragmentEntity = new AlertContentFragmentEntity();
                        ContentFragment contentFragment = child.adaptTo(ContentFragment.class);
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
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        LOGGER.debug("List That will be rendered to client side : {}", fragmentEntities);
        return fragmentEntities;
    }

    private ResourceResolver getResourceResolver() throws LoginException {
        Map<String, Object> map = new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER);
        return resourceResolverFactory.getServiceResourceResolver(map);
    }

}
