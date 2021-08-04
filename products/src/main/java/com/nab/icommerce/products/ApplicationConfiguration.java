package com.nab.icommerce.products;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.nab.icommerce.products.entities.Product;
import com.nab.icommerce.products.models.ProductQueryRequest;
import com.nab.icommerce.products.repositories.ProductItemRepository;
import com.nab.icommerce.products.repositories.ProductItemRepositoryImpl;
import com.nab.icommerce.products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
@EnableConfigurationProperties(Config.class)
public class ApplicationConfiguration {
    @Bean
    public ProductService productService() {
        return new ProductService();
    }

    @Bean
    public Function<Message<ProductQueryRequest>, Message<List<Product>>> queryProductFunction(@Autowired ProductService productService) {
        return productService::queryProduct;
    }

    @Bean
    public Supplier<Message<List<Product>>> getAllProductsFunction(@Autowired ProductService productService) {
        return productService::getAllProducts;
    }

    @Bean
    public Function<Message<Product>, Message<List<Product>>> getProductFunction(@Autowired ProductService productService) {
        return productService::getProduct;
    }

    @Bean
    public IDynamoDBMapper dynamoDBMapper(@Autowired AmazonDynamoDB dbClient) {
        return new DynamoDBMapper(dbClient);
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
    public ProductItemRepository productItemRepository(@Autowired IDynamoDBMapper dynamoDBMapper) {
        return new ProductItemRepositoryImpl(dynamoDBMapper);
    }
}
