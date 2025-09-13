package com.github.gannimatt.ecommerce.service;

import com.github.gannimatt.ecommerce.dto.CartItemRequest;
import com.github.gannimatt.ecommerce.dto.CartResponse;

public interface CartService {
    CartResponse getCart(String userEmail);
    CartResponse addItem(String userEmail, CartItemRequest req);
    CartResponse removeItem(String userEmail, Long productId);
    void clear(String userEmail);
}