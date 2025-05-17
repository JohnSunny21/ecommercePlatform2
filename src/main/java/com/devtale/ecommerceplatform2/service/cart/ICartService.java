package com.devtale.ecommerceplatform2.service.cart;

import com.devtale.ecommerceplatform2.model.Cart;
import com.devtale.ecommerceplatform2.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
