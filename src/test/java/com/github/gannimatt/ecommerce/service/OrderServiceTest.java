package com.github.gannimatt.ecommerce.service;

import com.github.gannimatt.ecommerce.dto.OrderResponse;
import com.github.gannimatt.ecommerce.entity.*;
import com.github.gannimatt.ecommerce.repository.CartRepository;
import com.github.gannimatt.ecommerce.repository.OrderRepository;
import com.github.gannimatt.ecommerce.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private CartRepository carts;
    private OrderRepository orders;
    private PaymentService payment;
    private OrderService service;

    @BeforeEach
    void setUp() {
        carts = mock(CartRepository.class);
        orders = mock(OrderRepository.class);
        payment = mock(PaymentService.class);
        service = new OrderServiceImpl(carts, orders, payment);
    }

    @Test
    void checkout_shouldSnapshotPricesAndClearCart() {
        String email = "u@e";
        Cart cart = new Cart();
        cart.setUserEmail(email);

        Product p = Product.builder().id(1L).sku("S").name("N").price(new BigDecimal("5.00")).build();
        CartItem ci = new CartItem();
        ci.setCart(cart);
        ci.setProduct(p);
        ci.setQuantity(2);
        cart.getItems().add(ci);

        when(carts.findByUserEmail(email)).thenReturn(Optional.of(cart));
        when(payment.processPayment(eq(email), any())).thenReturn(true);
        when(orders.save(any())).thenAnswer(inv -> inv.getArgument(0));

        OrderResponse resp = service.checkout(email);

        assertEquals(new BigDecimal("10.00"), resp.total());
        assertEquals(1, resp.items().size());
        assertEquals(0, cart.getItems().size());
        verify(carts).save(cart);
    }
}


