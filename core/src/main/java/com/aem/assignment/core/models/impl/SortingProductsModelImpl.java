package com.aem.assignment.core.models.impl;

import com.aem.assignment.core.models.SortingProductsModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = SortingProductsModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SortingProductsModelImpl implements SortingProductsModel {



}
