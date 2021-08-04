package com.nab.icommerce.shoppingcart.api;

import com.nab.icommerce.shoppingcart.entities.CartItem;
import com.nab.icommerce.shoppingcart.models.RetrieveCartRequestModel;
import com.nab.icommerce.shoppingcart.repositories.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.util.List;

public class FunctionalHandler {
    private CartItemRepository itemRepository;

    public FunctionalHandler(@Autowired CartItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Message<List<CartItem>> addToCart(Message<CartItem> cartItemMessage) {
        this.itemRepository.save(cartItemMessage.getPayload());
        Message<List<CartItem>> response =
                new GenericMessage<>(this.itemRepository.getCartItemsByUserId(cartItemMessage.getPayload().getUserId()));

        return response;
    }

    /**
     * Lambda handler that remove the specified cart item
     * @param cartItemMessage with at least fields userId and addedDate specified
     * @return
     */
    public Message<List<CartItem>> deleteCartItem(Message<CartItem> cartItemMessage) {
        this.itemRepository.delete(cartItemMessage.getPayload());
        Message<List<CartItem>> response =
                new GenericMessage<>(this.itemRepository.getCartItemsByUserId(cartItemMessage.getPayload().getUserId()));

        return response;
    }

    /**
     * Lambda handler that retrieve cart items of a specified user by userId
     * @param userIdMessage
     * @return the user's cart items
     */
    public Message<List<CartItem>> retrieveCart(Message<RetrieveCartRequestModel> RetrieveCartRequestMessage) {
        return new GenericMessage<>(this.itemRepository.getCartItemsByUserId(RetrieveCartRequestMessage.getPayload().getUserId()));
    }
}
