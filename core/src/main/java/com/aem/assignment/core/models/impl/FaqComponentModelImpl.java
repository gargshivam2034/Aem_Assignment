package com.aem.assignment.core.models.impl;

import com.aem.assignment.core.entities.FaqEntity;
import com.aem.assignment.core.models.FaqComponentModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Model(adaptables = {SlingHttpServletRequest.class},
        adapters = {FaqComponentModel.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FaqComponentModelImpl implements FaqComponentModel {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FaqComponentModel.class);

    List<FaqEntity> faqList = new ArrayList<>();


    @ChildResource
    Resource faqFields;

    public List<FaqEntity> getFaqList() {
        return faqList;
    }

    public void setFaqList(List<FaqEntity> faqList) {
        this.faqList = faqList;
    }

    public Resource getFaqFields() {
        return faqFields;
    }

    public void setFaqFields(Resource faqFields) {
        this.faqFields = faqFields;
    }

    /**
     * Initializes the FAQ component by populating the FAQ list.
     * This method extracts FAQ questions and answers from the provided resource and populates the FAQ list.
     */
    @PostConstruct
    public void init(){
        if(faqFields!=null && faqFields.hasChildren()){
            for(Resource res: faqFields.getChildren()){
                FaqEntity faqEntity = new FaqEntity();
                ValueMap valueMap = res.getValueMap();
                if(valueMap.containsKey("faqQuestion")){
                    faqEntity.setQuestion(valueMap.get("faqQuestion", String.class));
                }

                if(valueMap.containsKey("faqAnswer")){
                    faqEntity.setAnswer(valueMap.get("faqAnswer", String.class));
                }

                LOGGER.debug("FAQ Entity to be added in faqList: {}",faqEntity);
                faqList.add(faqEntity);
            }
        }
    }

}
