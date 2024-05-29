package com.aem.assignment.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.aem.assignment.core.entities.PairEntity;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.collections.iterators.TransformIterator;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Servlet for generating a dropdown DataSource from child pages of a specified path.
 */
@Component(service = Servlet.class, property =
        {
                ServletResolverConstants.SLING_SERVLET_METHODS + "=GET",
                ServletResolverConstants.SLING_SERVLET_PATHS + "=/apps/CustomServlet"
        }
)
public class DropDown extends SlingSafeMethodsServlet {

    /**
     * Handles HTTP GET requests to generate a dropdown DataSource.
     *
     * @param request  The Sling HTTP servlet request object.
     * @param response The Sling HTTP servlet response object.
     * @throws ServletException If an error occurs during servlet processing.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            String path = "/content/we-retail";
            List<PairEntity> pairList = getPagePairs(request.getResourceResolver(), path);
            DataSource dataSource = createDataSource(request.getResourceResolver(), pairList);
            request.setAttribute(DataSource.class.getName(), dataSource);
        } catch (Exception e) {
            // Handle exception
        }
    }

    /**
     * Retrieves pairs of titles and names from child pages of the specified path.
     *
     * @param resourceResolver The ResourceResolver instance.
     * @param path             The path to the parent page.
     * @return List of PairEntity objects representing page titles and names.
     */
    private List<PairEntity> getPagePairs(ResourceResolver resourceResolver, String path) {
        List<PairEntity> pairList = new ArrayList<>();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        if (pageManager != null) {
            Page parentPage = pageManager.getPage(path);
            if (parentPage != null) {
                Iterator<Page> listChildren = parentPage.listChildren();
                while (listChildren.hasNext()) {
                    Page childPage = listChildren.next();
                    String title = childPage.getTitle();
                    String name = childPage.getName();
                    pairList.add(new PairEntity(title, name));
                }
            }
        }
        return pairList;
    }

    /**
     * Creates a DataSource from a list of PairEntity objects.
     *
     * @param resourceResolver The ResourceResolver instance.
     * @param pairList         List of PairEntity objects representing page titles and names.
     * @return DataSource generated from the pairList.
     */
    private DataSource createDataSource(ResourceResolver resourceResolver, List<PairEntity> pairList) {
        return new SimpleDataSource(new TransformIterator(pairList.iterator(), input -> {
            PairEntity pair = (PairEntity) input;
            ValueMap valueMap = new ValueMapDecorator(new HashMap<>());
            valueMap.put("value", pair.getValue());
            valueMap.put("text", pair.getValue());
            return new ValueMapResource(resourceResolver, new ResourceMetadata(), JcrConstants.NT_UNSTRUCTURED, valueMap);
        }));
    }
}
