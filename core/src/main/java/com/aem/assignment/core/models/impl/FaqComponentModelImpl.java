package com.aem.assignment.core.models.impl;
import com.aem.assignment.core.entities.FaqEntity;
import com.aem.assignment.core.models.FaqComponentModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
@Model(adaptables = {Resource.class},
        adapters = {FaqComponentModel.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FaqComponentModelImpl implements FaqComponentModel {
    @ChildResource
    private  List<FaqEntity> faqFields;

    public List<FaqEntity> getFaqFields() {
        return faqFields;
    }



}