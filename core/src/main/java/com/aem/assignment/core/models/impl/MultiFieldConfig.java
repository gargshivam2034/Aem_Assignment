package com.aem.assignment.core.models.impl;

import com.aem.assignment.core.entities.Entity;
import com.aem.assignment.core.services.MultiFieldConfigService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import java.util.ArrayList;
import java.util.List;

@Model(adaptables =  SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MultiFieldConfig {

    @OSGiService
    private MultiFieldConfigService multiFieldConfigService;

    @SlingObject
    ResourceResolver resourceResolver;

    private final List<Entity> booklist = new ArrayList<>();
    public List<Entity> getMultiFieldConfigService()
    {

        Resource container = resourceResolver.getResource("/content/aem_assignment/us/en/jcr:content/root/container/container");
        if(container!=null&&container.hasChildren())
        {
            for(Resource resource:container.getChildren())
            {
                Resource books=resource.getChild("books");
                if (books != null && books.hasChildren()) {
                    for (Resource childresResource : books.getChildren()) {
                        ValueMap map = childresResource.getValueMap();
                        Entity bk = new Entity(map.get("bookAuthor", String.class),map.get("bookName", String.class));

                        String str=map.get("bookPrice", String.class);


                        StringBuilder stringBuilder = new StringBuilder();
                        int count = 0;

                        for (int i = str.length() - 1; i >= 0; i--) {
                            if (count % 3 == 0 && i != str.length() - 1) {
                                stringBuilder.insert(0, ',');
                            }
                            stringBuilder.insert(0, str.charAt(i));
                            count++;
                        }

                        String result = stringBuilder.toString();
                        bk.setBookPrice(result);
                        bk.setBookPath(map.get("bookPath", String.class));
                        booklist.add(bk);
                    }
                }


            }


        }
        return booklist;

    }

    public void setMultiFieldConfigService(MultiFieldConfigService multiFieldConfigService) {
        this.multiFieldConfigService = multiFieldConfigService;
    }

}