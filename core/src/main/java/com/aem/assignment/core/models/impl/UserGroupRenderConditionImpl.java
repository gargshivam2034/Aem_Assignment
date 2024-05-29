package com.aem.assignment.core.models.impl;

import com.adobe.granite.ui.components.rendercondition.RenderCondition;
import com.adobe.granite.ui.components.rendercondition.SimpleRenderCondition;
import com.aem.assignment.core.models.UserGroupRenderCondition;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;
import java.util.Iterator;

/**
 * Implementation of the UserGroupRenderCondition model that checks if the current user
 * belongs to a specified user group and sets a render condition accordingly.
 */
@Model(adaptables = SlingHttpServletRequest.class,
        adapters = UserGroupRenderCondition.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class UserGroupRenderConditionImpl implements UserGroupRenderCondition {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupRenderCondition.class);

    @ScriptVariable
    private SlingHttpServletRequest request;

    @ValueMapValue
    private String group;

    @SlingObject
    private ResourceResolver resourceResolver;

    private boolean isInContentAuthorGroup = false;

    /**
     * Initializes the model by checking if the current user belongs to the specified group.
     * If the user is a member of the group, sets the render condition attribute in the request.
     */
    @PostConstruct
    void init() {
        UserManager userManager = resourceResolver.adaptTo(UserManager.class);
        LOGGER.debug("Current User manager: {}", userManager);

        if (userManager == null) {
            return;
        }

        try {
            Authorizable currentUser = userManager.getAuthorizable(resourceResolver.getUserID());
            LOGGER.debug("Current user is: {}", currentUser);

            if (currentUser != null) {
                Iterator<Group> groupIterator = currentUser.memberOf();
                while (groupIterator.hasNext()) {
                    Group userGroup = groupIterator.next();
                    String groupId = userGroup.getID();
                    if (group.equals(groupId)) {
                        isInContentAuthorGroup = true;
                        break;
                    }
                }
            }
        } catch (RepositoryException e) {
            LOGGER.error("RepositoryException occurred while checking user group membership", e);
            throw new RuntimeException("Failed to check user group membership", e);
        }

        LOGGER.debug("Is current user a member of content author group: {}", isInContentAuthorGroup);

        request.setAttribute(RenderCondition.class.getName(), new SimpleRenderCondition(isInContentAuthorGroup));
    }
}
