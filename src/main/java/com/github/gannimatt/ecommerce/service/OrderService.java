package com.github.gannimatt.ecommerce.service;

import com.github.gannimatt.ecommerce.dto.OrderResponse;
import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse checkout(String userEmail); // creates order from cart, mock pay
    Page<OrderResponse> listMyOrders(String userEmail, Pageable pageable);
    OrderResponse getMyOrder(String userEmail, Long orderId);
}