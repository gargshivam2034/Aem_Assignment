package com.aem.assignment.core.models.impl;

import com.aem.assignment.core.models.PagePropertyModel;
import com.day.cq.wcm.api.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@Model(adaptables = {SlingHttpServletRequest.class},
        adapters = {PagePropertyModel.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PagePropertyModelImpl implements PagePropertyModel {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(PagePropertyModel.class);


    @ScriptVariable
    Page currentPage;

    private static final String INHERITED_SCRIPT_PROPERTY = "customInheritedScript";

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    String customInheritedScript;


    /**
     * Retrieves the custom inherited script for the current page.
     * If the custom inherited script is present on the current page, it returns that.
     * Otherwise, it retrieves the value from the ancestor pages.
     * @return The custom inherited script.
     */
    public String getCustomInheritedScript(){
        if(StringUtils.isNotBlank(customInheritedScript)){
            LOGGER.debug("Script present on the current page: ",customInheritedScript);
            return customInheritedScript;
        }

        return getPropertyFromPageOrAncestor(INHERITED_SCRIPT_PROPERTY);

    }

    @PostConstruct
    public void init(){

    }


    /**
     * Retrieves a property value from the current page or its ancestors.
     * @param propertyName The name of the property.
     * @return The value of the property.
     */
    public String getPropertyFromPageOrAncestor(String propertyName) {
        String propertyValue = currentPage.getProperties().get(propertyName, String.class);
        if (propertyValue == null && currentPage.getParent() != null) {
            propertyValue = getPropertyFromAncestors(currentPage.getParent(), propertyName);
        }
        LOGGER.debug("property Value inherited from parent page: {} ",propertyValue);
        return propertyValue;
    }


    /**
     * Recursively retrieves a property value from the ancestor pages.
     * @param page The current page in the hierarchy.
     * @param propertyName The name of the property.
     * @return The value of the property.
     */
    private String getPropertyFromAncestors(Page page, String propertyName) {
        String propertyValue = page.getProperties().get(propertyName, String.class);

        if (propertyValue == null && page.getParent() != null) {
            propertyValue = getPropertyFromAncestors(page.getParent(), propertyName);
        }

        return propertyValue;
    }
}
