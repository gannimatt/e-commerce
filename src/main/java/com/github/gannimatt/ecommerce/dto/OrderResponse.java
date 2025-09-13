package com.github.gannimatt.ecommerce.dto;

import com.github.gannimatt.ecommerce.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        Instant createdAt,
        OrderStatus status,
        BigDecimal total,
        List<OrderLine> items
) {
    public record OrderLine(
            Long productId,
            String sku,
            String name,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal lineTotal
    ) {}
}
