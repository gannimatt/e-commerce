package com.github.gannimatt.ecommerce.mapper;

import com.github.gannimatt.ecommerce.dto.OrderResponse;
import com.github.gannimatt.ecommerce.entity.Order;
import com.github.gannimatt.ecommerce.entity.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public final class OrderMapper {

    private OrderMapper() {}

    public static OrderResponse toResponse(Order order) {
        List<OrderResponse.OrderLine> items = order.getItems().stream()
                .map(OrderMapper::toOrderLine)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getTotal(),
                items
        );
    }

    private static OrderResponse.OrderLine toOrderLine(OrderItem oi) {
        var p = oi.getProduct();
        return new OrderResponse.OrderLine(
                p.getId(),
                p.getSku(),
                p.getName(),
                oi.getQuantity(),
                oi.getUnitPrice(),   // snapshot stored at checkout
                oi.getLineTotal()
        );
    }
}
