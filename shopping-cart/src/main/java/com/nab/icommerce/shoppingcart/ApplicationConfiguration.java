package com.nab.icommerce.shoppingcart;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.nab.icommerce.shoppingcart.api.FunctionalHandler;
import com.nab.icommerce.shoppingcart.entities.CartItem;
import com.nab.icommerce.shoppingcart.models.RetrieveCartRequestModel;
import com.nab.icommerce.shoppingcart.repositories.CartItemRepository;
import com.nab.icommerce.shoppingcart.repositories.CartItemRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.function.Function;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public FunctionalHandler apiHander(@Autowired CartItemRepository itemRepository){
        return new FunctionalHandler(itemRepository);
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB(@Autowired Environment environment) {
        AmazonDynamoDBClientBuilder clientBuilder = AmazonDynamoDBClientBuilder.standard();

        String dynamoDBTableRegion = environment.getProperty("app.dynamodb.region");
        String dynamoDBEndpoint = environment.getProperty("app.dynamodb.endpoint");

        System.out.println("app.dynamodb.region : " + dynamoDBTableRegion);
        System.out.println("app.dynamodb.endpoint : " + dynamoDBEndpoint);

        if (dynamoDBTableRegion != null && dynamoDBEndpoint == null) {
            clientBuilder.withRegion(dynamoDBTableRegion);
        }

        if (dynamoDBTableRegion != null && dynamoDBEndpoint != null) {
            clientBuilder.withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration(dynamoDBEndpoint, dynamoDBTableRegion));
        }

        return clientBuilder.build();
    }

    @Bean
    public IDynamoDBMapper dynamoDBMapper(@Autowired AmazonDynamoDB amazonDynamoDBClient){
        return new DynamoDBMapper(amazonDynamoDBClient);
    }

    @Bean
    public CartItemRepository cartItemRepository(@Autowired IDynamoDBMapper dynamoDBMapper){
        return new CartItemRepositoryImpl(dynamoDBMapper);
    }

    @Bean
    public Function<Message<RetrieveCartRequestModel>, Message<List<CartItem>>> retrieveCartFunction(@Autowired FunctionalHandler apiHandlers){
        return apiHandlers::retrieveCart;
    }

    @Bean
    public Function<Message<CartItem>, Message<List<CartItem>>> saveCartItemFunction(@Autowired FunctionalHandler apiHandlers){
        return apiHandlers::addToCart;
    }

    @Bean
    public Function<Message<CartItem>, Message<List<CartItem>>> deleteCartItemFunction(@Autowired FunctionalHandler apiHandlers){
        return apiHandlers::deleteCartItem;
    }
}
