package com.aem.assignment.core.models.impl;

import com.aem.assignment.core.entities.ProductEntity;
import com.aem.assignment.core.models.ProductCarousalModel;
import com.aem.assignment.core.services.ProductDetailService;
import com.aem.assignment.core.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = {SlingHttpServletRequest.class},
        adapters = {ProductCarousalModel.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductCarousalModelImpl implements ProductCarousalModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCarousalModel.class);

    @OSGiService
    ProductDetailService productDetailService;

    private static final String BASE_URL = "https://fakestoreapi.com/products/";


    //------Sort By--------

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    String sortFiltersType;

    List<ProductEntity> productEntityList = new ArrayList<>();

    /**
     * Retrieves the sorted list of product entities based on the specified sort type.
     * @return The sorted list of product entities.
     */
    public List<ProductEntity> getProductEntityList() {
        CommonUtils.sortProductEntitiesByPrice(productEntityList, sortFiltersType);
        return productEntityList;
    }


    /**
     * Initializes the product carousel model by fetching the product list based on the provided base URL.
     * It logs the fetched product list for debugging purposes.
     */
    @PostConstruct
    public void init(){
        productEntityList = productDetailService.getProductList(BASE_URL);
        LOGGER.debug("productEntityList fetched based on Sort type authored by author: {} ",productEntityList);
    }

}
