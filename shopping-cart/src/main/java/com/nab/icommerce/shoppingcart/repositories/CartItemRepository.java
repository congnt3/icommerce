package com.nab.icommerce.shoppingcart.repositories;

import com.nab.icommerce.shoppingcart.entities.CartItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, String> {
    List<CartItem> getCartItemsByUserId(String userId);
}
