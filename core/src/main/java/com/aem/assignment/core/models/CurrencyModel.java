package com.aem.assignment.core.models;

import com.aem.assignment.core.caconfig.CurrencyConfig;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class)
public class CurrencyModel {

    @Self
    private Resource resource;

    private String currencyCode;
    private double exchangeRate;

    @PostConstruct
    protected void init() {
        
        CurrencyConfig config = resource.adaptTo(ConfigurationBuilder.class).as(CurrencyConfig.class);

        if (config != null) {
            this.currencyCode = config.currencyCode();
            this.exchangeRate = config.exchangeRate();
        }
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }
}
