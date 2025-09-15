package com.github.gannimatt.ecommerce.service.impl;

import com.github.gannimatt.ecommerce.dto.CartItemRequest;
import com.github.gannimatt.ecommerce.dto.CartResponse;
import com.github.gannimatt.ecommerce.entity.*;
import com.github.gannimatt.ecommerce.mapper.CartMapper;
import com.github.gannimatt.ecommerce.repository.CartRepository;
import com.github.gannimatt.ecommerce.repository.ProductRepository;
import com.github.gannimatt.ecommerce.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    private final CartRepository carts;
    private final ProductRepository products;

    public CartServiceImpl(CartRepository carts, ProductRepository products) {
        this.carts = carts; this.products = products;
    }

    private Cart getOrCreate(String email) {
        return carts.findByUserEmail(email).orElseGet(() -> {
            Cart c = new Cart();
            c.setUserEmail(email);
            return carts.save(c);
        });
    }

    @Override
    public CartResponse getCart(String email) {
        Cart c = getOrCreate(email);
        return CartMapper.toResponse(c);
    }

    @Override
    public CartResponse addItem(String email, CartItemRequest req) {
        Cart c = getOrCreate(email);
        Product p = products.findById(req.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        CartItem item = c.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(p.getId()))
                .findFirst().orElse(null);

        if (item == null) {
            item = new CartItem();
            item.setCart(c);
            item.setProduct(p);
            item.setQuantity(req.quantity());
            c.getItems().add(item);
        } else {
            item.setQuantity(item.getQuantity() + req.quantity());
        }
        return CartMapper.toResponse(c);
    }

    @Override
    public CartResponse removeItem(String email, Long productId) {
        Cart c = getOrCreate(email);
        c.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        return CartMapper.toResponse(c);
    }

    @Override
    public void clear(String email) {
        getOrCreate(email).clear();
    }
}
