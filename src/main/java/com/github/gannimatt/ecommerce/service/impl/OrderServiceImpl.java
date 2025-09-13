package com.github.gannimatt.ecommerce.service.impl;

import com.github.gannimatt.ecommerce.dto.OrderResponse;
import com.github.gannimatt.ecommerce.entity.*;
import com.github.gannimatt.ecommerce.repository.CartRepository;
import com.github.gannimatt.ecommerce.repository.OrderRepository;
import com.github.gannimatt.ecommerce.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final CartRepository carts;
    private final OrderRepository orders;

    public OrderServiceImpl(CartRepository carts, OrderRepository orders) {
        this.carts = carts; this.orders = orders;
    }

    @Override
    public OrderResponse checkout(String email) {
        Cart cart = carts.findByUserEmail(email)
                .orElseThrow(() -> new IllegalStateException("Cart empty"));
        if (cart.getItems().isEmpty()) throw new IllegalStateException("Cart empty");

        Order o = new Order();
        o.setUserEmail(email);
        o.setCreatedAt(Instant.now());
        o.setStatus(OrderStatus.PAID); // mock payment success

        BigDecimal total = BigDecimal.ZERO;

        for (var ci : cart.getItems()) {
            OrderItem oi = new OrderItem();
            oi.setOrder(o);
            oi.setProduct(ci.getProduct());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getUnitPrice()); // BigDecimal
            oi.setLineTotal(ci.getUnitPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
            o.getItems().add(oi);

            total = total.add(oi.getLineTotal());
        }

        o.setTotal(total);

        orders.save(o);
        cart.clear(); // empty cart after checkout
        return toResponse(o);
    }

    @Override
    public Page<OrderResponse> listMyOrders(String email, Pageable pageable) {
        return orders.findByUserEmailOrderByCreatedAtDesc(email, pageable).map(this::toResponse);
    }

    @Override
    public OrderResponse getMyOrder(String email, Long orderId) {
        Order o = orders.findById(orderId)
                .filter(or -> or.getUserEmail().equals(email))
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return toResponse(o);
    }

    private OrderResponse toResponse(Order o) {
        var items = o.getItems().stream().map(oi ->
                new OrderResponse.OrderLine(
                        oi.getProduct().getId(),
                        oi.getProduct().getSku(),
                        oi.getProduct().getName(),
                        oi.getQuantity(),
                        oi.getUnitPrice(),
                        oi.getLineTotal()
                )
        ).collect(Collectors.toList());
        return new OrderResponse(o.getId(), o.getCreatedAt(), o.getStatus(), o.getTotal(), items);
    }
}
