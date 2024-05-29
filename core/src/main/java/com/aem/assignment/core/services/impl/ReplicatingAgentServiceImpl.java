package com.aem.assignment.core.services.impl;

import com.aem.assignment.core.services.ReplicatingAgentService;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component(service = ReplicatingAgentService.class)
public class ReplicatingAgentServiceImpl implements ReplicatingAgentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplicatingAgentServiceImpl.class);
    private static final String API_URL = "https://fakestoreapi.com/products/1";
    private static final String PAGE_PATH = "/content/aem_assignment/us/en";
    private static final String PAGE_NAME = "MyCustomPage";
    private static final String PAGE_TEMPLATE = "/conf/aem_assignment/settings/wcm/templates/demo-template";

    @Override
    public String ApiFetchData() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            return executeHttpGet(httpClient, API_URL);
        } catch (IOException e) {
            LOGGER.error("Error in creating HTTP client: {}", e.getMessage(), e);
            throw new RuntimeException("Error in creating HTTP client: " + e.getMessage(), e);
        }
    }

    /**
     * Executes an HTTP GET request and returns the response body.
     *
     * @param httpClient the HTTP client to use for the request.
     * @param apiUrl the URL to send the request to.
     * @return the response body as a String.
     * @throws IOException if an I/O error occurs.
     */
    private String executeHttpGet(CloseableHttpClient httpClient, String apiUrl) throws IOException {
        HttpGet httpGet = new HttpGet(apiUrl);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            return handleResponse(response);
        } catch (IOException e) {
            LOGGER.error("Error in executing HTTP request: {}", e.getMessage(), e);
            throw new RuntimeException("Error in executing HTTP request: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the HTTP response, checking the status code and extracting the response body.
     *
     * @param response the HTTP response to handle.
     * @return the response body as a String.
     * @throws IOException if an I/O error occurs.
     */
    private String handleResponse(CloseableHttpResponse response) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode < 300) {
            return EntityUtils.toString(response.getEntity());
        } else {
            LOGGER.error("Received non-success status code: {}", statusCode);
            throw new RuntimeException("Received non-success status code: " + statusCode);
        }
    }

    @Override
    public void CreatePage(ResourceResolver resourceResolver) {
        try {
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            if (pageManager != null) {
                createPage(pageManager, PAGE_PATH, PAGE_NAME, PAGE_TEMPLATE);
            } else {
                LOGGER.error("Failed to adapt resource resolver to PageManager");
                throw new RuntimeException("Failed to adapt resource resolver to PageManager");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to create the page: {}", e.getMessage(), e);
        }
    }

    /**
     * Creates a new page with the specified parameters.
     *
     * @param pageManager the PageManager to use for creating the page.
     * @param parentPath the path of the parent page.
     * @param pageName the name of the new page.
     * @param templatePath the template path to use for the new page.
     * @return the created page.
     * @throws Exception if an error occurs during page creation.
     */
    private Page createPage(PageManager pageManager, String parentPath, String pageName, String templatePath) throws Exception {
        return pageManager.create(parentPath, pageName, templatePath, pageName);
    }
}
