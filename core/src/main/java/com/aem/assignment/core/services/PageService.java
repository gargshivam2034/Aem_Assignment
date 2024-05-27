package com.aem.assignment.core.services;

import com.aem.assignment.core.entities.ProductEntity;

public interface PageService {

    /**
     * Creates a page based on the provided product entity.
     *
     * @param productEntity The product entity to use for creating the page.
     */
    void createPage(ProductEntity productEntity);

}
