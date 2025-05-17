package com.devtale.ecommerceplatform2.service.cart;

import com.devtale.ecommerceplatform2.model.CartItem;

public interface ICartItemService {

    void addItemToCart(Long cartId, Long productId,int quantity);
    void removeItemFromCart(Long cartId,Long productId);
    void updateItemQuantity(Long cartId,Long productId,int quantity);
    CartItem getCartItem(Long cartId, Long productId);
}
