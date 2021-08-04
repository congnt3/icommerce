package com.nab.icommerce.products.models;

import lombok.Getter;
import lombok.Setter;

public class ProductQueryRequest {
    @Getter
    @Setter
    private String category;

    @Getter
    @Setter
    private ConditionDefinition price;

    @Getter
    @Setter
    private String brand;

    @Getter
    @Setter
    private String colour;
}
