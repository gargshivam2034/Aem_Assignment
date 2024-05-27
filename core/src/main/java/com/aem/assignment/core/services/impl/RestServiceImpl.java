package com.aem.assignment.core.services.impl;

import com.aem.assignment.core.CommonConstants;
import com.aem.assignment.core.bean.ClientResponse;
import com.aem.assignment.core.services.RestService;
import com.aem.assignment.core.utils.CommonUtils;
import org.osgi.service.component.annotations.Component;


@Component(service = {RestService.class}, immediate = true)
public class RestServiceImpl implements RestService {
    @Override
    public ClientResponse getProductDetails(String mainUrl) throws Exception {
        return CommonUtils.getClientResponse
                (CommonConstants.GET,mainUrl,null
                        ,null);
    }

    @Override
    public ClientResponse getProductDetailList(String mainUrl) throws Exception {
        return CommonUtils.getClientResponse
                (CommonConstants.GET,mainUrl,null,null);
    }
}
