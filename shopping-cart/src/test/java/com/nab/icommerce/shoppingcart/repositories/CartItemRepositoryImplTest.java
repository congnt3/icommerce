package com.nab.icommerce.shoppingcart.repositories;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nab.icommerce.shoppingcart.ApplicationConfiguration;
import com.nab.icommerce.shoppingcart.entities.CartItem;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = ApplicationConfiguration.class)
class CartItemRepositoryImplTest {
    DynamoDBMapper mapperMock;
    CartItemRepository cartItemRepository;

    @BeforeEach
    void beforeEach(){
        this.mapperMock = mock(DynamoDBMapper.class);
        this.cartItemRepository = new CartItemRepositoryImpl(mapperMock);
    }

    @Test
    void save() {
        CartItem item = new CartItem();
        item.setAddedDate(DateTime.now().getMillis());
        item.setProductUuid(UUID.randomUUID().toString());
        item.setQuantity(3);
        item.setUserId("Generated_User" + UUID.randomUUID().toString());

        this.cartItemRepository.save(item);

        verify(this.mapperMock).save(item);
    }

    @Test
    void saveAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void existsById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findAllById() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void delete() {
        CartItem item = new CartItem();
        item.setAddedDate(DateTime.now().getMillis());
        item.setProductUuid(UUID.randomUUID().toString());
        item.setQuantity(3);
        item.setUserId("Generated_User" + UUID.randomUUID().toString());

        this.cartItemRepository.delete(item);

        verify(this.mapperMock).delete(item);
    }

    @Test
    void deleteAllById() {
    }

    @Test
    void deleteAll() {
    }

    @Test
    void testDeleteAll() {
    }
}
