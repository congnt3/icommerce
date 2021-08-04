package com.nab.icommerce.products.services;

import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.nab.icommerce.products.entities.Product;
import com.nab.icommerce.products.models.ConditionDefinition;

import java.util.List;
import java.util.Map;

public interface ProductFilterStrategy {
    List<Product> doFilter(Map<String, ConditionDefinition> filterConditions, IDynamoDBMapper dynamodbMapper);
}
