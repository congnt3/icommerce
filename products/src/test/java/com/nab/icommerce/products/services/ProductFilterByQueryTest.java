package com.nab.icommerce.products.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.nab.icommerce.products.constants.ProductTableMetadata;
import com.nab.icommerce.products.entities.Product;
import com.nab.icommerce.products.models.ConditionDefinition;
import static org.junit.jupiter.api.Assertions.*;

import com.nab.icommerce.products.models.ProductQueryRequest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class ProductFilterByQueryTest {

    @Test
    void doFilterCheckQueryExecuted() {
        ProductFilterByQuery queryFilter = new ProductFilterByQuery();
        ProductFilterByQuery spyQueryFilter = spy(queryFilter);
        DynamoDBMapper mockMapper = mock(DynamoDBMapper.class);

        Map<String, ConditionDefinition> filterConditions = new HashMap<>();
        ConditionDefinition conditionDefinition = new ConditionDefinition();
        conditionDefinition.setValue("test-category");
        conditionDefinition.setComparisonOperand("=");
        filterConditions.put(ProductTableMetadata.FIELD_CATEGORY, conditionDefinition);

        spyQueryFilter.doFilter(filterConditions,
                mockMapper);

        verify(spyQueryFilter).getProductQueryExpression(filterConditions, ProductTableMetadata.FIELD_CATEGORY);

        DynamoDBQueryExpression<Product> queryExpression = queryFilter.getProductQueryExpression(filterConditions, ProductTableMetadata.FIELD_CATEGORY);

        //verify query method called on mapper object
        verify(mockMapper).query(eq(Product.class), any());
    }

    @Test
    void getProductQueryExpression() {
        Map<String, ConditionDefinition> filterConditions = new HashMap<>();
        ConditionDefinition conditionDefinition = new ConditionDefinition();
        conditionDefinition.setValue("test-category");
        conditionDefinition.setComparisonOperand("=");
        filterConditions.put(ProductTableMetadata.FIELD_CATEGORY, conditionDefinition);

        ProductFilterByQuery queryFilter = new ProductFilterByQuery();
        DynamoDBQueryExpression queryExpression = queryFilter.getProductQueryExpression(filterConditions, ProductTableMetadata.FIELD_CATEGORY);

        assertEquals(queryExpression.getIndexName(), ProductTableMetadata.GIS_BY_CATEGORY, "Wrong Index name selected");
    }

    @Test
    void getProductQueryExpressionWithHashAndFilterKey() {
        Map<String, ConditionDefinition> filterConditions = new HashMap<>();
        ConditionDefinition conditionDefinition = new ConditionDefinition();
        conditionDefinition.setValue("test-category");
        conditionDefinition.setComparisonOperand("=");
    }

    @Test
    void canPerformQuery() {
        Map<String, ConditionDefinition> filterConditions = new HashMap<>();
        ConditionDefinition conditionDefinition = new ConditionDefinition();
        conditionDefinition.setValue("test-category");
        conditionDefinition.setComparisonOperand("=");
        filterConditions.put(ProductTableMetadata.FIELD_CATEGORY, conditionDefinition);

        assertTrue(ProductFilterByQuery.canDoFilter(filterConditions), "Filter by category should be able to executed by query");
    }

    @Test
    void cannotPerformQuery() {
        Map<String, ConditionDefinition> filterConditions = new HashMap<>();
        ConditionDefinition conditionDefinition = new ConditionDefinition();
        conditionDefinition.setValue("https://www.google.com/some/test/images.jpg");
        conditionDefinition.setComparisonOperand("=");
        filterConditions.put(ProductTableMetadata.FIELD_IMAGE_PATH, conditionDefinition);

        assertFalse(ProductFilterByQuery.canDoFilter(filterConditions), "Filter by image page field should not be able to executed by query");
    }

    @Test
    void doFilter() {
        Map<String, ConditionDefinition> filterConditions = new HashMap<>();
        ConditionDefinition conditionDefinition = new ConditionDefinition();
        conditionDefinition.setValue("Accessories");
        conditionDefinition.setComparisonOperand("=");
        filterConditions.put(ProductTableMetadata.FIELD_CATEGORY, conditionDefinition);

        ConditionDefinition priceCondition = new ConditionDefinition();
        priceCondition.setValue("100");
        priceCondition.setComparisonOperand("<");
        filterConditions.put(ProductTableMetadata.FIELD_PRICE, priceCondition);

        ProductFilterByQuery queryFilter = new ProductFilterByQuery();
        DynamoDBQueryExpression queryExpression = queryFilter.getProductQueryExpression(filterConditions, ProductTableMetadata.FIELD_CATEGORY);

        assertEquals(queryExpression.getIndexName(), ProductTableMetadata.GIS_BY_CATEGORY, "Wrong Index name selected");

    }
}