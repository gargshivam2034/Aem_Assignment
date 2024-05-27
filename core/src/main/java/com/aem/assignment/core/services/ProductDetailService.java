package com.aem.assignment.core.services;

import com.aem.assignment.core.entities.ProductEntity;

import java.util.List;

public interface ProductDetailService {

    /**
     * Retrieves the product data for a given main URL.
     *
     * @param mainURL The main URL from which to retrieve product data.
     * @return The product entity containing the data retrieved.
     */
    ProductEntity getProductsData(String mainURL);

    /**
     * Retrieves a list of product entities from the specified main URL.
     *
     * @param mainUrl The main URL from which to retrieve the list of products.
     * @return A list of product entities.
     */
    List<ProductEntity> getProductList(String mainUrl);
}
