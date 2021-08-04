package com.nab.icommerce.products.services;

import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.nab.icommerce.products.constants.ProductTableMetadata;
import com.nab.icommerce.products.entities.Product;
import com.nab.icommerce.products.models.ComparisonOperands;
import com.nab.icommerce.products.models.ConditionDefinition;
import com.nab.icommerce.products.models.ProductQueryRequest;
import com.nab.icommerce.products.repositories.ProductItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.util.*;

public class ProductService {
    @Autowired
    private ProductItemRepository repository;

    public Message<List<Product>> queryProduct(Message<ProductQueryRequest> queryRequest) {
        List<Product> queryResult = new ArrayList<>();

        Map<String, ConditionDefinition> queryFilter = new HashMap<>();
        if (queryRequest.getPayload().getCategory() != null) {

            queryFilter.put(ProductTableMetadata.FIELD_CATEGORY, new ConditionDefinition(ComparisonOperands.EQUALS, queryRequest.getPayload().getCategory()));
        }

        if (queryRequest.getPayload().getPrice() != null) {
            queryFilter.put(ProductTableMetadata.FIELD_PRICE, queryRequest.getPayload().getPrice());
        }

        if (queryRequest.getPayload().getBrand() != null) {
            queryFilter.put(ProductTableMetadata.FIELD_BRAND, new ConditionDefinition(ComparisonOperands.EQUALS, queryRequest.getPayload().getBrand()));
        }

        if (queryRequest.getPayload().getColour() != null) {
            queryFilter.put(ProductTableMetadata.FIELD_COLOUR, new ConditionDefinition(ComparisonOperands.EQUALS, queryRequest.getPayload().getColour()));
        }

        ProductFilterStrategy productFilterStrategy = null;
        if (ProductFilterByQuery.canDoFilter(queryFilter)) {
            productFilterStrategy = new ProductFilterByQuery();
        } else {
            productFilterStrategy = new ProductFilterByScan();
        }

        queryResult = this.repository.findAll(queryFilter, productFilterStrategy);
        return new GenericMessage<>(queryResult);
    }

    public Message<List<Product>> getAllProducts() {
        return new GenericMessage(this.repository.findAll());
    }

    public Message<List<Product>> getProduct(Message<Product> uuidMessage) {
        List<Product> loadResults = new ArrayList<>();
        Optional<Product> loadResult = this.repository.findById(uuidMessage.getPayload().getUuid());
        if (loadResult.isPresent()) {
            loadResults.add(loadResult.get());
        }

        return new GenericMessage(loadResults);
    }
}

