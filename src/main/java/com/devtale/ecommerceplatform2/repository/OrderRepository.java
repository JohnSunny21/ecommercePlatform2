package com.devtale.ecommerceplatform2.repository;

import com.devtale.ecommerceplatform2.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);
}
