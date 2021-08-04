package com.nab.icommerce.products.repositories;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.nab.icommerce.products.constants.ProductTableMetadata;
import com.nab.icommerce.products.entities.Product;
import com.nab.icommerce.products.models.ConditionDefinition;
import com.nab.icommerce.products.services.ProductFilterByQuery;
import com.nab.icommerce.products.services.ProductFilterByScan;
import com.nab.icommerce.products.services.ProductFilterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductItemRepositoryImpl implements ProductItemRepository {

    private IDynamoDBMapper dynamodbMapper;

    public ProductItemRepositoryImpl(IDynamoDBMapper mapper) {
        this.dynamodbMapper = mapper;
    }

    @Override
    public <S extends Product> S save(S s) {
        this.dynamodbMapper.save(s);

        return this.dynamodbMapper.load(s);
    }

    @Override
    public <S extends Product> Iterable<S> saveAll(Iterable<S> iterable) {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public Optional<Product> findById(String s) {
        Product hashKey = new Product();
        hashKey.setUuid(s);
        Product loadResult = this.dynamodbMapper.load(hashKey);

        return loadResult != null ? Optional.of(loadResult) : Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public Iterable<Product> findAll() {
        return this.dynamodbMapper.scan(Product.class, new DynamoDBScanExpression());
    }

    @Override
    public Iterable<Product> findAllById(Iterable<String> iterable) {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public void deleteById(String s) {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public void delete(Product product) {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public void deleteAllById(Iterable<? extends String> iterable) {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public void deleteAll(Iterable<? extends Product> iterable) {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("This method is not supported");
    }

    /**
     * query the product table using the condition specified in the filterCondition
     * there must be at least a condition for one of following attributes
     * the other query conditions will be set in the filterExpression
     *
     * @param filterConditions
     * @return
     */
    public List<Product> findAll(Map<String, ConditionDefinition> filterConditions, ProductFilterStrategy filterStrategy) {
        return filterStrategy.doFilter(filterConditions, this.dynamodbMapper);
    }
}
