package com.aem.assignment.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.collections.iterators.TransformIterator;
import org.apache.commons.collections4.KeyValue;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.mozilla.javascript.EcmaError;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Component(service = Servlet.class,property =
        {
                //ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES + "=apps/CustomServlet",
                ServletResolverConstants.SLING_SERVLET_METHODS + "=GET",
                ServletResolverConstants.SLING_SERVLET_PATHS + "=/apps/CustomServlet"
        }
)
public class  DropDown extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException
    {

        try
        {
            String path="/content/we-retail";
            List<Pair> pairList=new ArrayList<>();
            ResourceResolver resourceResolver=request.getResourceResolver();
            PageManager pageManager=resourceResolver.adaptTo(PageManager.class);
            if(pageManager==null)
                return;
            Page parentPage=pageManager.getPage(path);
           Iterator<Page> listChildren=  parentPage.listChildren();
           while (listChildren.hasNext())
           {
              Page childPage= listChildren.next();
             String title= childPage.getTitle();
             String name= childPage.getName();
             pairList.add(new Pair(title,name));
           }
            DataSource dataSource=new SimpleDataSource(new TransformIterator(pairList.iterator(),input->
            {
                Pair pair=(Pair) input;
                ValueMap valueMap= new ValueMapDecorator(new HashMap<>());
                valueMap.put("value", pair.key);
                valueMap.put("text",pair.value);
                return new ValueMapResource(resourceResolver,new ResourceMetadata(), JcrConstants.NT_UNSTRUCTURED,valueMap);
            }
            ));
           request.setAttribute(DataSource.class.getName(),dataSource);
        }
        catch (Exception e)
        {

        }



    }


}
class Pair
{
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Pair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    String value;

}

