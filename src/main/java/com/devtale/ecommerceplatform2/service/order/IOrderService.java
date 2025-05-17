package com.devtale.ecommerceplatform2.service.order;

import com.devtale.ecommerceplatform2.dto.OrderDto;
import com.devtale.ecommerceplatform2.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
    OrderDto convertToDto(Order order);
}
