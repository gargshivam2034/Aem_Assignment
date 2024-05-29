package com.aem.assignment.core.utils;

import com.google.gson.Gson;
import com.aem.assignment.core.CommonConstants;
import com.aem.assignment.core.bean.ClientResponse;
import com.aem.assignment.core.entities.ProductEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.apache.oltu.oauth2.common.message.types.TokenType.BEARER;

/**
 * Utility class for common operations such as HTTP requests, sorting, and URL parameter extraction.
 */
public final class CommonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * Performs an HTTP GET or POST request and returns the response.
     *
     * @param method        The HTTP method type (GET or POST).
     * @param apiUrl        The API URL to send the request to.
     * @param requestObject The request object to be sent in case of a POST request.
     * @param token         The authorization token.
     * @param <T>           The type of the request object.
     * @return The client response containing status code and data.
     * @throws Exception If an error occurs during the request.
     */
    public static <T> ClientResponse getClientResponse(final String method,
                                                       final String apiUrl,
                                                       final T requestObject,
                                                       final String token) throws Exception {
        ClientResponse clientResponse = new ClientResponse();

        if (StringUtils.isAllBlank(method, apiUrl)) {
            return clientResponse;
        }
        if (method.equals(CommonConstants.GET)) {
            final HttpGet httpGet = new HttpGet(apiUrl);
            httpGet.setHeader(CommonConstants.CONTENT_TYPE, CommonConstants.CONTENT_TYPE_JSON);
            if (token != null) {
                httpGet.setHeader(HttpHeaders.AUTHORIZATION, BEARER + token);
            }
            clientResponse = executeRequest(httpGet);
        } else if (method.equals(CommonConstants.POST)) {
            final HttpPost httpPost = new HttpPost(apiUrl);
            String requestBody = new Gson().toJson(requestObject);
            if (StringUtils.isNotBlank(requestBody)) {
                httpPost.addHeader(CommonConstants.CONTENT_TYPE, CommonConstants.CONTENT_TYPE_JSON);
                try {
                    httpPost.setEntity(new StringEntity(requestBody));
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("Unsupported Encoding Exception: ", e);
                }
            }
            clientResponse = executeRequest(httpPost);
        }
        return clientResponse;
    }

    /**
     * Executes an HTTP request and returns the response.
     *
     * @param request The HTTP request to execute.
     * @return The client response containing status code and data.
     * @throws Exception If an error occurs during the request execution.
     */
    public static ClientResponse executeRequest(final HttpUriRequest request) throws Exception {
        String result = StringUtils.EMPTY;
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse httpResponse = httpClient.execute(request)) {
            HttpEntity httpEntity = httpResponse.getEntity();
            ClientResponse clientResponse = new ClientResponse();
            clientResponse.setStatusCode(httpResponse.getStatusLine().getStatusCode());
            if (httpEntity != null) {
                result = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
                clientResponse.setData(result);
                EntityUtils.consume(httpEntity);
            }
            return clientResponse;
        } catch (ClientProtocolException e) {
            LOGGER.error("Client Protocol Exception: ", e);
        } catch (IOException e) {
            LOGGER.error("IO Exception: ", e);
        }
        return null;
    }

    /**
     * Sorts a list of ProductEntity objects based on their price.
     *
     * @param productEntityList The list of ProductEntity objects to sort.
     * @param sortOrder         The sort order ('asc' for ascending, 'dsc' for descending).
     * @throws IllegalArgumentException If an invalid sort order is provided.
     */
    public static void sortProductEntitiesByPrice(List<ProductEntity> productEntityList, String sortOrder) {
        if (!"asc".equalsIgnoreCase(sortOrder) && !"dsc".equalsIgnoreCase(sortOrder)) {
            throw new IllegalArgumentException("Invalid sort order. Use 'asc' or 'dsc'.");
        }

        productEntityList.sort((product1, product2) -> {
            double price1 = product1.getPrice();
            double price2 = product2.getPrice();
            return "asc".equalsIgnoreCase(sortOrder) ? Double.compare(price1, price2) : Double.compare(price2, price1);
        });
    }

    /**
     * Extracts parameters from the URL suffix in an AEM request.
     *
     * @param request The Sling HTTP request.
     * @return A list of string parameters from the URL suffix.
     */
    public static List<String> getParamsFromURL(final SlingHttpServletRequest request) {
        RequestPathInfo requestPathInfo = request.getRequestPathInfo();
        String suffix = requestPathInfo.getSuffix();
        if (StringUtils.isNotBlank(suffix)) {
            String[] paramsArray = suffix.split("/");
            List<String> params = new ArrayList<>(Arrays.asList(paramsArray));
            if (!params.isEmpty()) {
                params.remove(0); // Remove the first element if necessary
            }
            return params;
        }
        return new ArrayList<>();
    }
}
