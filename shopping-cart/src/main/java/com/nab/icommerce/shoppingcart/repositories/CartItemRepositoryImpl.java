package com.nab.icommerce.shoppingcart.repositories;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.util.StringUtils;
import com.nab.icommerce.shoppingcart.entities.CartItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CartItemRepositoryImpl implements CartItemRepository {
    public CartItemRepositoryImpl(@Autowired IDynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    private IDynamoDBMapper mapper;

    @Override
    public <S extends CartItem> S save(S s) {
        mapper.save(s);
        S savedItem = mapper.load(s);

        return savedItem;
    }

    @Override
    public <S extends CartItem> Iterable<S> saveAll(Iterable<S> iterable) {
        mapper.batchSave(iterable);
        Map<String, List<Object>> loadResult = mapper.batchLoad(iterable);
        String annotatedTableName = CartItem.class.getAnnotation(DynamoDBTable.class).tableName();
        String tableName = !StringUtils.isNullOrEmpty(annotatedTableName) ? annotatedTableName : CartItem.class.getName();

        List<Object> savedCartItems = loadResult.get(tableName);
        if (savedCartItems == null) {
            return null;
        }

        return savedCartItems.parallelStream().map(obj -> (S) obj).collect(Collectors.toList());
    }

    @Override
    public Optional<CartItem> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public Iterable<CartItem> findAll() {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public Iterable<CartItem> findAllById(Iterable<String> iterable) {
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
    public void delete(CartItem cartItem) {
        mapper.delete(cartItem);
    }

    @Override
    public void deleteAllById(Iterable<? extends String> iterable) {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public void deleteAll(Iterable<? extends CartItem> iterable) {
        mapper.batchDelete(iterable);
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public PaginatedQueryList<CartItem> getCartItemsByUserId(String userId) {
        DynamoDBQueryExpression<CartItem> queryExpression = new DynamoDBQueryExpression<>();
        CartItem hashKey = new CartItem();
        hashKey.setUserId(userId);
        queryExpression.setHashKeyValues(hashKey);

        return mapper.query(CartItem.class, queryExpression);
    }
}
