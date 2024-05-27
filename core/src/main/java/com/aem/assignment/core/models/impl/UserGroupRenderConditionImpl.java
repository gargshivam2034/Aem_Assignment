package com.aem.assignment.core.models.impl;

import com.adobe.granite.ui.components.rendercondition.RenderCondition;
import com.adobe.granite.ui.components.rendercondition.SimpleRenderCondition;
import com.aem.assignment.core.models.UserGroupRenderCondition;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;
import java.util.Iterator;


@Model(adaptables = SlingHttpServletRequest.class
        ,adapters = UserGroupRenderCondition.class
        ,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class UserGroupRenderConditionImpl implements UserGroupRenderCondition {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(UserGroupRenderCondition.class);

    @ScriptVariable
    SlingHttpServletRequest request;

    @ValueMapValue
    private String group;

    @SlingObject
    private ResourceResolver resourceResolver;

    boolean isInContentAuthorGroup = false;


    @PostConstruct
    void init(){
        UserManager userManager = resourceResolver
                .adaptTo(UserManager.class);
        LOGGER.debug("Current User manager: {}", userManager);
        if(userManager == null) return;

        boolean isInContentAuthorGroup = false;

        try{
            Authorizable currentUser = userManager
                    .getAuthorizable(resourceResolver.getUserID());
            LOGGER.debug("Current user is: ",currentUser);
            Iterator<Group> groupIterator = currentUser.memberOf();
            while(groupIterator.hasNext()){
                Group userGroup = groupIterator.next();
                String groupId = userGroup.getID();
                if(group.equals(groupId)){
                    isInContentAuthorGroup = true;
                    break;
                }
            }

        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }

        LOGGER.debug("is Current user is a member of Author content group: {}"
                ,isInContentAuthorGroup);

        request.setAttribute(RenderCondition.class
                .getName(),new SimpleRenderCondition(
                        isInContentAuthorGroup)
        );
    }

}
