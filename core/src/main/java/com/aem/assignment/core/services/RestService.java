package com.aem.assignment.core.services;

import com.aem.assignment.core.bean.ClientResponse;

public interface RestService {

    /**
     * Retrieves product details from the specified main URL via a RESTful API call.
     *
     * @param mainUrl The main URL from which to retrieve product details.
     * @return A ClientResponse object containing the response from the API call.
     * @throws Exception If an error occurs during the API call.
     */
    ClientResponse getProductDetails(String mainUrl) throws Exception;

    /**
     * Retrieves a list of product details from the specified main URL via a RESTful API call.
     *
     * @param mainUrl The main URL from which to retrieve the list of product details.
     * @return A ClientResponse object containing the response from the API call.
     * @throws Exception If an error occurs during the API call.
     */
    ClientResponse getProductDetailList(String mainUrl) throws Exception;

}
