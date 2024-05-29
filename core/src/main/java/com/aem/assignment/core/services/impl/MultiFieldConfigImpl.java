package com.aem.assignment.core.services.impl;

import com.aem.assignment.core.entities.Entity;
import com.aem.assignment.core.services.MultiFieldConfigService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


@Component(service = MultiFieldConfigService.class)
public class MultiFieldConfigImpl implements MultiFieldConfigService {

    @Reference
    ResourceResolverFactory resourceResolverFactory;
     private final List<Entity> booklist = new ArrayList<>();

     @SlingObject
     ResourceResolver resourceResolver;

    @Override
    public List<Entity> getBookDetail()
    {
        try {
//            Map<String,Object> authmap=new HashMap<>();
//            authmap.put(ResourceResolverFactory.SUBS   ERVICE,"shivam");
//            authmap.put(ResourceResolverFactory.PASSWORD,"admin");
            //ResourceResolver resourceResolver=resourceResolverFactory.getResourceResolver(authmap);


            Resource books = resourceResolver.getResource("/content/aem_assignment/us/en/jcr:content/root/container/container/multifield_741242136/books");
            if (books != null && books.hasChildren()) {
                Stream<Resource> resourceStream = StreamSupport.stream(books.getChildren().spliterator(), false);
                return resourceStream.map(childResource -> {
                            ValueMap map = childResource.getValueMap();
                            Entity bk = new Entity(map.get("bookAuthor", String.class),
                                    map.get("bookName", String.class),
                                    map.get("bookPrice", String.class),
                                    map.get("bookPath", String.class));
                            return bk;
                        })
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return booklist;
    }

}
