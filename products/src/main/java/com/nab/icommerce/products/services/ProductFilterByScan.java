package com.nab.icommerce.products.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.nab.icommerce.products.entities.Product;
import com.nab.icommerce.products.models.ConditionDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Perform the given filter on Product table using Dynamodb Scan Operation
 */
public class ProductFilterByScan implements ProductFilterStrategy {
    @Override
    public List<Product> doFilter(Map<String, ConditionDefinition> filterConditions, IDynamoDBMapper dynamodbMapper) {
        DynamoDBScanExpression scanExpression = getDynamoDBScanExpression(filterConditions);

        return dynamodbMapper.scan(Product.class, scanExpression);
    }

    protected DynamoDBScanExpression getDynamoDBScanExpression(Map<String, ConditionDefinition> filterConditions) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        Map<String, AttributeValue> attValue = new HashMap<>();

        //Loop through the filter condition to build the filter expression
        StringBuilder filterExpressionBuilder = new StringBuilder();
        for (Map.Entry<String, ConditionDefinition> filterEntry : filterConditions.entrySet()) {
            String filterCondition = new StringBuilder(filterEntry.getKey())
                    .append(filterEntry.getValue().getComparisonOperand())
                    .append(ProductFilterUtils.buildArgumentName(filterEntry.getKey())).toString();

            if (filterExpressionBuilder.length() > 0) {
                filterExpressionBuilder.append(" and ");
            }

            filterExpressionBuilder.append(filterCondition);

            //Put respective Expression Attribute value to the attValue map
            attValue.put(ProductFilterUtils.buildArgumentName(filterEntry.getKey()), ProductFilterUtils.buildAttributeValue(filterEntry.getKey(), filterEntry.getValue().getValue()));
        }

        if (filterExpressionBuilder.length() > 0) {
            scanExpression
                    .withFilterExpression(filterExpressionBuilder.toString());
        }

        scanExpression.withExpressionAttributeValues(attValue);
        return scanExpression;
    }
}
