
package com.aem.assignment.core.caconfig;
import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(name = "Currency Configuration", description = "Configuration for currency settings based on locale")
public @interface CurrencyConfig {

    @Property(label = "Currency Code", description = "The currency code, e.g., USD, EUR")
    String currencyCode() default "USD";

    @Property(label = "Exchange Rate", description = "The exchange rate to base currency")
    double exchangeRate() default 1.0;
}
