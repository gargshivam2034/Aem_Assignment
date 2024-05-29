package com.aem.assignment.core.entities;


import com.aem.assignment.core.models.FaqComponentModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;


@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FaqEntity {
    @ValueMapValue
    private String faqQuestion;
    @ValueMapValue
    private String faqAnswer;

    public String getFaqQuestion() {
        return faqQuestion;
    }

    public String getFaqAnswer() {
        return faqAnswer;
    }

}
