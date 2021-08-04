package com.nab.icommerce.shoppingcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ShoppingCartApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ShoppingCartApplication.class, args);
    }
}
