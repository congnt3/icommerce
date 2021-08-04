package com.nab.icommerce.products;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.dynamodb")
public class Config {

    public String region;

    public String endpoint;
}
