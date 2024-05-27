package com.aem.assignment.core.caconfig;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(
        label = "Currency Configuration for Product Price",
        description = "AEM locale based currency config"
)
public @interface CurrencyCaConfig {

    @Property(label = "Currency for product price",
            description = "Enter currency for price of the product")
    String productCurrency() default StringUtils.EMPTY;
}
