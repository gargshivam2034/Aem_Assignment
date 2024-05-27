package com.aem.assignment.core.models;

import com.aem.assignment.core.entities.ProductEntity;
import com.aem.assignment.core.models.SingleProductModel;
import com.aem.assignment.core.services.ProductDetailService;
import com.aem.assignment.core.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Model(adaptables = {SlingHttpServletRequest.class},
        adapters = {SingleProductModel.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SingleProductModelImpl implements SingleProductModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleProductModel.class);

    private static final String BASE_URL = "https://fakestoreapi.com/products/";

    @ScriptVariable
    SlingHttpServletRequest request;

    @OSGiService
    ProductDetailService productDetailService;

    ProductEntity productEntity = new ProductEntity();

    @Default(values = StringUtils.EMPTY)
    String productId;

    /**
     * Retrieves the product entity based on the product ID extracted from the URL.
     *
     * @return The product entity retrieved based on the product ID.
     */
    public ProductEntity getProductEntity(){
        List<String> suffix = CommonUtils.getParamsFromURL(request);

        LOGGER.debug("suffix fetched from current URL: {}",suffix);

        if(suffix.size()==2){
            productId = suffix.get(1);
            productEntity = productDetailService
                    .getProductsData(BASE_URL+productId);

            LOGGER.debug("product entity based on the URL Suffix: {}", productEntity);

            return productEntity;
        }
        return new ProductEntity();
    }

}
