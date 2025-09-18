package com.github.gannimatt.ecommerce.service;

import com.github.gannimatt.ecommerce.dto.CartItemRequest;
import com.github.gannimatt.ecommerce.dto.CartResponse;
import com.github.gannimatt.ecommerce.entity.Cart;
import com.github.gannimatt.ecommerce.entity.CartItem;
import com.github.gannimatt.ecommerce.entity.Product;
import com.github.gannimatt.ecommerce.repository.CartRepository;
import com.github.gannimatt.ecommerce.repository.ProductRepository;
import com.github.gannimatt.ecommerce.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    private CartRepository carts;
    private ProductRepository products;
    private CartService service;

    @BeforeEach
    void setUp() {
        carts = mock(CartRepository.class);
        products = mock(ProductRepository.class);
        service = new CartServiceImpl(carts, products);
    }

    @Test
    void addItem_shouldMergeSameProduct() {
        String email = "u@e";
        Cart cart = new Cart();
        cart.setUserEmail(email);
        when(carts.findByUserEmail(email)).thenReturn(Optional.of(cart));

        Product p = Product.builder().id(10L).sku("S").name("N").price(new BigDecimal("2.50")).build();
        when(products.findById(10L)).thenReturn(Optional.of(p));

        service.addItem(email, new CartItemRequest(10L, 1));
        service.addItem(email, new CartItemRequest(10L, 2));

        assertEquals(1, cart.getItems().size());
        CartItem item = cart.getItems().get(0);
        assertEquals(3, item.getQuantity());

        verify(carts, atLeastOnce()).save(cart);
        CartResponse resp = service.getCart(email);
        assertEquals(new BigDecimal("7.50"), resp.total());
    }
}


