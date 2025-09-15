package com.github.gannimatt.ecommerce.mapper;

import com.github.gannimatt.ecommerce.dto.CartResponse;
import com.github.gannimatt.ecommerce.entity.Cart;
import com.github.gannimatt.ecommerce.entity.CartItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public final class CartMapper {

    private CartMapper() {}

    public static CartResponse toResponse(Cart cart) {
        List<CartResponse.CartLine> lines = cart.getItems().stream()
                .map(CartMapper::toLine)
                .collect(Collectors.toList());

        BigDecimal total = lines.stream()
                .map(CartResponse.CartLine::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(total, lines);
    }

    private static CartResponse.CartLine toLine(CartItem i) {
        var p = i.getProduct();
        var unitPrice = p.getPrice(); // dynamic: current catalog price
        var lineTotal = unitPrice.multiply(BigDecimal.valueOf(i.getQuantity()));
        return new CartResponse.CartLine(
                p.getId(),
                p.getSku(),
                p.getName(),
                i.getQuantity(),
                unitPrice,
                lineTotal
        );
    }
}
