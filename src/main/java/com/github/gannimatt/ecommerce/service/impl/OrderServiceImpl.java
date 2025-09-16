package com.github.gannimatt.ecommerce.service.impl;

import com.github.gannimatt.ecommerce.dto.OrderResponse;
import com.github.gannimatt.ecommerce.entity.*;
import com.github.gannimatt.ecommerce.mapper.OrderMapper;
import com.github.gannimatt.ecommerce.repository.CartRepository;
import com.github.gannimatt.ecommerce.repository.OrderRepository;
import com.github.gannimatt.ecommerce.service.OrderService;
import com.github.gannimatt.ecommerce.service.PaymentService;
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
    private final PaymentService payment;

    public OrderServiceImpl(CartRepository carts, OrderRepository orders, PaymentService payment) {
        this.carts = carts;
        this.orders = orders;
        this.payment = payment;
    }

    @Override
    @Transactional
    public OrderResponse checkout(String email) {
        Cart cart = carts.findByUserEmail(email)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order();
        order.setUserEmail(email);
        order.setCreatedAt(Instant.now());
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        // Snapshot the current product data into order items
        var orderItems = cart.getItems().stream().map(ci -> {
            Product p = ci.getProduct();

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(p);
            oi.setQuantity(ci.getQuantity());

            BigDecimal unit = p.getPrice();   // snapshot unit price at time of checkout
            oi.setUnitPrice(unit);
            oi.setLineTotal(unit.multiply(BigDecimal.valueOf(ci.getQuantity())));
            return oi;
        }).collect(Collectors.toList());

        order.setItems(orderItems);
        order.setTotal(orderItems.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        //Mock payment processing
        boolean paid = payment.processPayment(email, order.getTotal());
        order.setStatus(paid ? OrderStatus.PAID : OrderStatus.PENDING_PAYMENT);

        // Persist order
        Order saved = orders.save(order);

        // Clear cart after successful order creation
        cart.clear();
        carts.save(cart);

        return OrderMapper.toResponse(saved);
    }

    @Override
    public Page<OrderResponse> listMyOrders(String email, Pageable pageable) {
        return orders.findByUserEmailOrderByCreatedAtDesc(email, pageable).map(OrderMapper::toResponse);
    }

    @Override
    public OrderResponse getMyOrder(String email, Long orderId) {
        Order o = orders.findById(orderId)
                .filter(or -> or.getUserEmail().equals(email))
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return OrderMapper.toResponse(o);
    }

   
}
