package com.nab.icommerce.products.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.nab.icommerce.products.constants.ProductTableMetadata;
import com.nab.icommerce.products.entities.Product;
import com.nab.icommerce.products.models.ComparisonOperands;
import com.nab.icommerce.products.models.ConditionDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Perform the given filter on Product table using Query
 */
public class ProductFilterByQuery implements ProductFilterStrategy {
    @Override
    public List<Product> doFilter(Map<String, ConditionDefinition> filterConditions, IDynamoDBMapper dynamodbMapper) {

        //Find the usable hash key for the query
        String hashKeyName = findHashKey(filterConditions);

        if (hashKeyName == null) {
            throw new IllegalArgumentException("Input filter conditions does not contain any hash key or Global Secondary Index's primary key");
        }

        DynamoDBQueryExpression<Product> queryExpression = getProductQueryExpression(filterConditions, hashKeyName);

        return dynamodbMapper.query(Product.class, queryExpression);
    }

    /**
     * Build the QueryExpression to use
     * @param filterConditions
     * @param hashKeyName
     * @return
     */
    public DynamoDBQueryExpression<Product> getProductQueryExpression(Map<String, ConditionDefinition> filterConditions, String hashKeyName) {
        DynamoDBQueryExpression<Product> queryExpression = new DynamoDBQueryExpression<>();
        Map<String, AttributeValue> attValue = new HashMap<>();
        attValue.put(ProductFilterUtils.buildArgumentName(hashKeyName), ProductFilterUtils.buildAttributeValue(hashKeyName, filterConditions.get(hashKeyName).getValue()));

        Map<String, String> fieldToGSIMap = ProductTableMetadata.getFieldToGSIMap();

        String keyCondExpression = new StringBuilder(hashKeyName)
                .append(" = ")
                .append(ProductFilterUtils.buildArgumentName(hashKeyName)).toString();
        queryExpression.withIndexName(fieldToGSIMap.get(hashKeyName))
                .withConsistentRead(false)
                .withKeyConditionExpression(keyCondExpression);

        //Loop through the filter condition to build the filter expression
        StringBuilder filterExpressionBuilder = new StringBuilder();
        for (Map.Entry<String, ConditionDefinition> filterEntry : filterConditions.entrySet()) {
            if (!hashKeyName.equals(filterEntry.getKey())) {
                String filterCondition = new StringBuilder(filterEntry.getKey())
                        .append(filterEntry.getValue().getComparisonOperand())
                        .append(ProductFilterUtils.buildArgumentName(filterEntry.getKey())).toString();

                if (filterExpressionBuilder.length() > 0) {
                    filterExpressionBuilder.append(" and ");
                }

                filterExpressionBuilder.append(filterCondition);

                //Put respective Expression Attribute value to the attValue map
                attValue.put(ProductFilterUtils.buildArgumentName(filterEntry.getKey()), ProductFilterUtils.buildAttributeValue(filterEntry.getKey(), filterConditions.get(filterEntry.getKey()).getValue()));
            }
        }

        if (filterExpressionBuilder.length() > 0) {
            queryExpression
                    .withFilterExpression(filterExpressionBuilder.toString());
        }

        queryExpression.withExpressionAttributeValues(attValue);
        return queryExpression;
    }

    /**
     * Check whether this class able to perform the Query with the specified filter Condition
     * Based on the available Global Secondary Index
     * @param filterConditions
     * @return
     */
    public static Boolean canDoFilter(Map<String, ConditionDefinition> filterConditions) {
        String hashKeyName = findHashKey(filterConditions);

        return hashKeyName != null;
    }

    /**
     * Find the available hashkey to perform a query on any available Global Secondary Index
     * @param filterConditions
     * @return
     */
    protected static String findHashKey(Map<String, ConditionDefinition> filterConditions) {
        Map<String, String> fieldToGSIMap = ProductTableMetadata.getFieldToGSIMap();
        //Find the usable hash key for the query
        String hashKeyName = null;

        for (String availableHashKey : fieldToGSIMap.keySet()) {
            if (filterConditions.containsKey(availableHashKey)
                    && ComparisonOperands.EQUALS.equals(filterConditions.get(availableHashKey).getComparisonOperand())) {
                hashKeyName = availableHashKey;
                break;
            }
        }

        return hashKeyName;
    }
}
