package com.aem.assignment.core.services.impl;

import com.aem.assignment.core.services.ReplicatingAgentService;
import com.day.cq.wcm.api.PageManager;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;

import java.io.IOException;

@Component(service = ReplicatingAgentService.class)
public class ReplicatingAgentServiceImpl implements ReplicatingAgentService {
    @Override
    public String ApiFetchData() {
        String responseBody=new String();
        try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
            String apiUrl = "https://fakestoreapi.com/products/1";
            HttpGet httpGet = new HttpGet(apiUrl);

            try (CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet)) {
                int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
                if (statusCode >= 200 && statusCode < 300) {
                    responseBody = EntityUtils.toString(closeableHttpResponse.getEntity());
                    return responseBody;
                }
            } catch (IOException e) {
                throw new RuntimeException("Error in executing HTTP request: " + e.getMessage(), e);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error in creating HTTP client: " + e.getMessage(), e);
        }
        return responseBody;
    }

    @Override
    public void CreatePage(ResourceResolver resourceResolver) {
        try
        {
            PageManager pageManager= resourceResolver.adaptTo(PageManager.class);
            pageManager.create("/content/aem_assignment/us/en","MyCustomPage","/conf/aem_assignment/settings/wcm/templates/demo-template","MyCustomPage");
        }
        catch (Exception e)
        {

        }
    }
}
