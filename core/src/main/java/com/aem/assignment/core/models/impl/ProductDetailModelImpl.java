package com.aem.assignment.core.models.impl;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.aem.assignment.core.entities.ProductEntity;
import com.aem.assignment.core.models.ProductDetailModel;
import com.aem.assignment.core.services.ProductDetailService;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Model(adaptables = {SlingHttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        adapters = {ProductDetailModel.class})
public class ProductDetailModelImpl implements ProductDetailModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDetailModel.class);


    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    ProductDetailService productDetailService;

    @ScriptVariable
    Page currentPage;

    private static final String BASE_URL = "https://fakestoreapi.com/products/";

    ProductEntity productEntity = new ProductEntity();


    /**
     * Retrieves the product entity.
     * @return The product entity.
     */
    public ProductEntity getProductEntity(){
        return productEntity;
    }


    /**
     * Initializes the product detail model by fetching product data based on the current page's product ID.
     * It logs the fetched product data for debugging purposes.
     */
    @PostConstruct
    void init() {
        if (currentPage != null) {
            ValueMap pageProperties = currentPage.getProperties();
            String productId = pageProperties.get("productId", String.class);

            LOGGER.debug("Product Id fetched from current page property: {} ",productId);

            if(StringUtils.isNotEmpty(productId)){
                String mainURL = BASE_URL + productId;
                productEntity = productDetailService.getProductsData(mainURL);

                LOGGER.debug("Product fetched from product Id: {}",productEntity);
            }

        }
    }


}
