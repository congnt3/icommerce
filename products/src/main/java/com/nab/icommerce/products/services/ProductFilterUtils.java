package com.nab.icommerce.products.services;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.nab.icommerce.products.constants.ProductTableMetadata;

import java.util.Map;

public class ProductFilterUtils {
    /**
     * Private constructor to hide the default public constructor.
     */
    private ProductFilterUtils(){}

    /**
     * Lookup the field metadata to build the AttributeValue object to use for the query
     *
     * @param attName
     * @param attValue
     * @return
     */
    public static AttributeValue buildAttributeValue(String attName, String attValue) {
        String attType = "S";
        Map<String, String> fieldTypeMap = ProductTableMetadata.getFieldTypeMap();
        if (fieldTypeMap.containsKey(attName)) {
            attType = fieldTypeMap.get(attName);
        }

        switch (attType) {
            case "S":
                return new AttributeValue().withS(attValue);
            case "N":
                return new AttributeValue().withN(attValue);
            default:
                throw new IllegalArgumentException("Type does not supported");
        }
    }

    /**
     * Create the DynamoDBQueryExpression parameter name to use
     *
     * @param attName the attribute name that need to create DynamoDBQueryExpression parameter
     * @return the DynamoDBQueryExpression parameter name
     */
    public static String buildArgumentName(String attName) {
        return new StringBuilder(":").append(attName).toString();
    }
}
