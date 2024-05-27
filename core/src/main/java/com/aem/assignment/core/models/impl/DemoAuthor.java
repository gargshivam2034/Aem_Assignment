package com.aem.assignment.core.models.impl;


import com.aem.assignment.core.models.impl.Entity;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import com.day.cq.wcm.api.Page;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DemoAuthor
{
    @ValueMapValue
    private String firstName;

    @ValueMapValue
    private String lastName;

    @ValueMapValue
    private  String midName;

    @ValueMapValue
    private String contactNo;

    @ScriptVariable
    private Page currentPage;

    @RequestAttribute(name="rAttribute")
    private String requestAttribute;
    public String getContactNo() {
        return contactNo;
    }
    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPageTitle() {
        return currentPage.getTitle();
    }
    public String getPageName() {
        return currentPage.getName();
    }


    public String getMidNameuehufr() {
        return midName;
    }
    public String getRequestAttribute() {
        return requestAttribute;
    }
    @ChildResource
    private Resource books;

    private List<Entity> list = new ArrayList<>();
    public List<Entity> getList() {
        return list;
    }

    @PostConstruct
    protected void init() {
        if (books != null && books.hasChildren()) {
            for (Resource childres : books.getChildren()) {
                ValueMap map = childres.getValueMap();
                Entity bk = new Entity();
                bk.setBookName(map.get("bookName", String.class));
                bk.setBookAuthor(map.get("bookAuthor", String.class));
                bk.setBookPrice(map.get("bookPrice", String.class));
                list.add(bk);
            }
        }
    }

}
