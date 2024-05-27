package com.aem.assignment.core.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.aem.assignment.core.CommonConstants;
import com.aem.assignment.core.bean.ClientResponse;
import com.aem.assignment.core.entities.ProductEntity;
import com.aem.assignment.core.services.ProductDetailService;
import com.aem.assignment.core.services.RestService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Component(service = {ProductDetailService.class})
public class ProductDetailServiceImpl implements ProductDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDetailServiceImpl.class);


    @Reference
    RestService restService;

    @Override
    public ProductEntity getProductsData(String mainURL) {
        ProductEntity productEntity = new ProductEntity();

        try {
            ClientResponse clientResponse = restService
                    .getProductDetails(mainURL);
            LOGGER.debug("clientResponse form the restservice from the mainURL: {}", clientResponse);
            if (clientResponse != null
                    && clientResponse.getStatusCode()
                    == HttpServletResponse.SC_OK){
                JSONObject responseObj = new JSONObject(clientResponse.getData());
                ObjectMapper objectMapper = new ObjectMapper();
                productEntity = objectMapper.readValue(responseObj.toString(), ProductEntity.class);

                LOGGER.debug("product Entity object: {} ",productEntity);

            }
        } catch (Exception e) {
            LOGGER.debug("Failed to get the Product: ", e);
        }

        return productEntity;
    }

    @Override
    public List<ProductEntity> getProductList(String mainUrl) {
        List<ProductEntity> productEntityList = new ArrayList<>();
        try{
            ClientResponse clientResponse = restService.getProductDetailList(mainUrl);
            LOGGER.debug("clientResponse form the restservice from the mainURL: {}", clientResponse);
            if(clientResponse!=null &&
                    clientResponse.getStatusCode() == HttpServletResponse.SC_OK){
                String entityJson = clientResponse.getData();
                JSONArray jsonArray = new JSONArray(entityJson);
                if(jsonArray!=null){
                    ObjectMapper objectMapper = new ObjectMapper();
                    productEntityList = objectMapper.readValue(
                            String.valueOf(jsonArray),
                            new TypeReference<List<ProductEntity>>() {
                            }
                    );
                    LOGGER.debug("Product Entity list fetched from API: {}", productEntityList);
                }
            }
        } catch (Exception e) {
            LOGGER.debug("Failed to get the product list from API {}", e);
        }

        return productEntityList;
    }


}
