package com.devtale.ecommerceplatform2.repository;

import com.devtale.ecommerceplatform2.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    void deleteAllByCartId(Long id);
}
