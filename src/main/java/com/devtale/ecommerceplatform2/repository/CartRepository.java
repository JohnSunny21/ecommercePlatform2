package com.devtale.ecommerceplatform2.repository;

import com.devtale.ecommerceplatform2.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findByUserId(Long userId);
}
